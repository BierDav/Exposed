package org.jetbrains.exposed.v1.core

import org.jetbrains.exposed.v1.core.vendors.ColumnMetadata
import org.jetbrains.exposed.v1.core.vendors.DatabaseDialect
import org.jetbrains.exposed.v1.core.vendors.H2Dialect
import org.jetbrains.exposed.v1.core.vendors.MariaDBDialect
import org.jetbrains.exposed.v1.core.vendors.MysqlDialect
import org.jetbrains.exposed.v1.core.vendors.OracleDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.core.vendors.SQLServerDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialect
import org.jetbrains.exposed.v1.core.vendors.h2Mode
import java.math.BigDecimal

@Suppress(names = ["NestedBlockDepth", "ComplexMethod", "LongMethod"])
internal actual fun DatabaseDialect.dbDefaultToString(
    column: Column<*>,
    exp: Expression<*>
): String = when (exp) {
    is LiteralOp<*> -> {
        when (val value = exp.value) {
            is Boolean -> when (this) {
                is MysqlDialect -> if (value) "1" else "0"
                is PostgreSQLDialect -> value.toString()
                else -> dataTypeProvider.booleanToStatementString(value)
            }
            is String -> when {
                this is PostgreSQLDialect -> when (column.columnType) {
                    is VarCharColumnType -> "'$value'::character varying"
                    is TextColumnType -> "'$value'::text"
                    else -> dataTypeProvider.processForDefaultValue(exp)
                }
                this is OracleDialect || h2Mode == H2Dialect.H2CompatibilityMode.Oracle -> when {
                    column.columnType is VarCharColumnType && value == "" -> "NULL"
                    column.columnType is TextColumnType && value == "" -> "NULL"
                    else -> value
                }
                else -> value
            }
            is Enum<*> -> when (exp.columnType) {
                is EnumerationNameColumnType<*> -> when (this) {
                    is PostgreSQLDialect -> "'${value.name}'::character varying"
                    else -> value.name
                }
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is BigDecimal -> when (this) {
                is MysqlDialect -> value.setScale((exp.columnType as DecimalColumnType).scale).toString()
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is Byte -> when {
                this is PostgreSQLDialect && value < 0 -> "'${dataTypeProvider.processForDefaultValue(exp)}'::integer"
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is Short -> when {
                this is PostgreSQLDialect && value < 0 -> "'${dataTypeProvider.processForDefaultValue(exp)}'::integer"
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is Int -> when {
                this is PostgreSQLDialect && value < 0 -> "'${dataTypeProvider.processForDefaultValue(exp)}'::integer"
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is Long -> when {
                this is SQLServerDialect && (value < 0 || value > Int.MAX_VALUE.toLong()) ->
                    "${dataTypeProvider.processForDefaultValue(exp)}."
                this is PostgreSQLDialect && (value < 0 || value > Int.MAX_VALUE.toLong()) ->
                    "'${dataTypeProvider.processForDefaultValue(exp)}'::bigint"
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is UInt -> when {
                this is SQLServerDialect && value > Int.MAX_VALUE.toUInt() -> "${dataTypeProvider.processForDefaultValue(exp)}."
                this is PostgreSQLDialect && value > Int.MAX_VALUE.toUInt() -> "'${dataTypeProvider.processForDefaultValue(exp)}'::bigint"
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            is ULong -> when {
                this is SQLServerDialect && value > Int.MAX_VALUE.toULong() -> "${dataTypeProvider.processForDefaultValue(exp)}."
                this is PostgreSQLDialect && value > Int.MAX_VALUE.toULong() -> "'${dataTypeProvider.processForDefaultValue(exp)}'::bigint"
                else -> dataTypeProvider.processForDefaultValue(exp)
            }
            else -> {
                when {
                    column.columnType is JsonColumnMarker -> {
                        val processed = dataTypeProvider.processForDefaultValue(exp)
                        when (this) {
                            is PostgreSQLDialect -> {
                                if (column.columnType.usesBinaryFormat) {
                                    processed.replace(Regex("(\"|})(:|,)(\\[|\\{|\")"), "$1$2 $3")
                                } else {
                                    processed
                                }
                            }
                            is MariaDBDialect -> processed.trim('\'')
                            is MysqlDialect -> "_utf8mb4\\'${processed.trim('(', ')', '\'')}\\'"
                            else -> when {
                                processed.startsWith('\'') && processed.endsWith('\'') -> processed.trim('\'')
                                else -> processed
                            }
                        }
                    }
                    column.columnType is ArrayColumnType<*, *> && this is PostgreSQLDialect -> {
                        (value as List<*>)
                            .takeIf { it.isNotEmpty() }
                            ?.run {
                                val delegateColumnType = column.columnType.delegate as IColumnType<Any>
                                val delegateColumn = (column as Column<Any?>).withColumnType(delegateColumnType)
                                val processed = map {
                                    if (delegateColumn.columnType is StringColumnType) {
                                        "'$it'::text"
                                    } else {
                                        dbDefaultToString(delegateColumn, delegateColumn.asLiteral(it))
                                    }
                                }
                                "ARRAY$processed"
                            } ?: dataTypeProvider.processForDefaultValue(exp)
                    }
                    column.columnType is IDateColumnType -> {
                        val processed = dataTypeProvider.processForDefaultValue(exp)
                        if (processed.startsWith('\'') && processed.endsWith('\'')) {
                            processed.trim('\'')
                        } else {
                            processed
                        }
                    }
                    else -> dataTypeProvider.processForDefaultValue(exp)
                }
            }
        }
    }
    is Function<*> -> {
        var processed = dataTypeProvider.processForDefaultValue(exp)
        if (exp.columnType is IDateColumnType) {
            if (processed.startsWith("CURRENT_TIMESTAMP") || processed == "GETDATE()") {
                when (this) {
                    is SQLServerDialect -> processed = "getdate"
                    is MariaDBDialect -> processed = processed.lowercase()
                }
            }
            if (processed.trim('(').startsWith("CURRENT_DATE")) {
                when (this) {
                    is MysqlDialect -> processed = "curdate()"
                }
            }
        }
        processed
    }
    else -> dataTypeProvider.processForDefaultValue(exp)
}

internal actual fun SchemaUtilityApi.isIncorrectSizeOrScale(
    columnMeta: ColumnMetadata,
    columnType: IColumnType<*>
): Boolean {
    // ColumnMetadata.scale can only be non-null if ColumnMetadata.size is non-null
    if (columnMeta.size == null) return false
    val dialect = currentDialect
    return when (columnType) {
        is DecimalColumnType -> columnType.precision != columnMeta.size || columnType.scale != columnMeta.scale
        is CharColumnType -> columnType.colLength != columnMeta.size
        is VarCharColumnType -> columnType.colLength != columnMeta.size
        is BinaryColumnType -> if (dialect is PostgreSQLDialect || dialect.h2Mode == H2Dialect.H2CompatibilityMode.PostgreSQL) {
            false
        } else {
            columnType.length != columnMeta.size
        }
        else -> false
    }
}
