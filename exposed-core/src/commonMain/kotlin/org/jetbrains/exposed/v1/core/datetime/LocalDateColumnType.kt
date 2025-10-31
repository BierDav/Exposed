package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.IDateColumnType
import org.jetbrains.exposed.v1.core.statements.api.RowApi
import org.jetbrains.exposed.v1.core.vendors.H2Dialect
import org.jetbrains.exposed.v1.core.vendors.OracleDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.core.vendors.SQLiteDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialect
import org.jetbrains.exposed.v1.core.vendors.h2Mode
import kotlin.time.Instant

private fun formatDate(instant: Instant, timeZone: TimeZone): String {
    return LocalDate.Formats.ISO.format(instant.toLocalDateTime(timeZone).date)
}

private fun oracleDateLiteral(instant: Instant): String {
    val formatted = formatDate(instant, TimeZone.currentSystemDefault())
    return "TO_DATE('$formatted', 'YYYY-MM-DD')"
}

/**
 * Base column type for storing date values without time components.
 *
 * This abstract class provides the foundation for date-only columns across different database dialects.
 * It handles the conversion between application date types and database representations, ensuring
 * proper formatting and parsing based on the current database dialect.
 *
 * @param T The application-specific date type (e.g., [LocalDate], kotlinx.datetime.LocalDate)
 * @see IDateColumnType
 * @see KotlinLocalDateColumnType
 */
abstract class LocalDateColumnType<T> : ColumnType<T>(), IDateColumnType {
    abstract fun toLocalDate(value: T): LocalDate

    abstract fun fromLocalDate(value: LocalDate): T

    override val hasTimePart: Boolean = false

    override fun sqlType(): String = currentDialect.dataTypeProvider.dateType()

    override fun nonNullValueToString(value: T & Any): String {
        val localDate = toLocalDate(value)
        val instant = localDate.atStartOfDayIn(TimeZone.currentSystemDefault())

        return if (currentDialect is OracleDialect) {
            oracleDateLiteral(instant)
        } else {
            "'${formatDate(instant, TimeZone.currentSystemDefault())}'"
        }
    }



    override fun valueFromDB(value: Any): T? {
        return fromLocalDate(localDateValueFromDB(value))
    }



    override fun notNullValueToDB(value: T & Any): Any {
        val localDate = toLocalDate(value)
        val instant = localDate.atStartOfDayIn(TimeZone.currentSystemDefault())

        return when {
            currentDialect is SQLiteDialect -> formatDate(instant, TimeZone.currentSystemDefault())
            else -> localDate
        }
    }

    override fun nonNullValueAsDefaultString(value: T & Any): String {
        return when (currentDialect) {
            is PostgreSQLDialect -> "${nonNullValueToString(value)}::date"
            else -> super.nonNullValueAsDefaultString(value)
        }
    }

    override fun readObject(rs: RowApi, index: Int): Any? {
        val dialect = currentDialect
        return if (dialect is OracleDialect || dialect.h2Mode == H2Dialect.H2CompatibilityMode.Oracle) {
            rs.getObject(index, Instant::class, this)
        } else {
            super.readObject(rs, index)
        }
    }
}

private fun LocalDate.atStartOfDayIn(timeZone: TimeZone): Instant {
    return this.atTime(LocalTime(0, 0, 0)).toInstant(timeZone)
}


internal expect fun localDateValueFromDB(value: Any): LocalDate

internal fun longToLocalDate(epochMillis: Long): LocalDate {
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}
