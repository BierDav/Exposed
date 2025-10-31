package org.jetbrains.exposed.v1.core

import kotlinx.io.asSource
import kotlinx.io.buffered
import org.jetbrains.exposed.v1.core.UuidColumnType.Companion.uuidRegexp
import org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import org.jetbrains.exposed.v1.core.statements.api.PreparedStatementApi
import org.jetbrains.exposed.v1.core.statements.api.RowApi
import org.jetbrains.exposed.v1.core.vendors.H2Dialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLNGDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialect
import java.io.InputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.sql.Blob
import java.sql.Clob
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.use
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

internal actual fun uuidValueFromDb(value: Any): Uuid = when {
    value is UUID -> value.toKotlinUuid()
    value is Uuid -> value
    value is ByteArray -> Uuid.fromByteArray(value)
    value is String && value.matches(uuidRegexp) -> Uuid.parse(value)
    value is String -> ByteBuffer.wrap(value.toByteArray()).let { b -> Uuid.fromLongs(b.long, b.long) }
    value is ByteBuffer -> Uuid.fromLongs(value.long, value.long)
    else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
}

/**
 * Binary column for storing BLOBs.
 */
class BlobColumnType(
    /** Returns whether an OID column should be used instead of BYTEA. This value only applies to PostgreSQL databases. */
    val useObjectIdentifier: Boolean = false
) : ColumnType<ExposedBlob>() {
    override fun sqlType(): String = when {
        useObjectIdentifier && currentDialect is PostgreSQLDialect -> "oid"
        useObjectIdentifier -> error("Storing BLOBs using OID columns is only supported by PostgreSQL")
        else -> currentDialect.dataTypeProvider.blobType()
    }

    override fun valueFromDB(value: Any): ExposedBlob = when (value) {
        is ExposedBlob -> value
        is InputStream -> ExposedBlob(value.asSource().buffered())
        is ByteArray -> ExposedBlob(value)
        is Blob -> ExposedBlob(value.binaryStream.asSource().buffered())
        is ByteBuffer -> ExposedBlob(value.array())
        else -> error("Unexpected value of type Blob: $value of ${value::class.qualifiedName}")
    }

    override fun nonNullValueToString(value: ExposedBlob): String {
        // For H2 Blobs the original dataTypeProvider must be taken (even if H2 in other DB mode)
        return ((currentDialect as? H2Dialect)?.originalDataTypeProvider ?: currentDialect.dataTypeProvider)
            .hexToDb(value.hexString())
    }

    override fun readObject(rs: RowApi, index: Int) = when {
        currentDialect is PostgreSQLDialect && useObjectIdentifier -> {
            rs.getObject(index, Blob::class)?.binaryStream?.let { ExposedBlob(it.asSource().buffered()) }
        }
        else -> rs.getObject(index)
    }

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        when (val toSetValue = (value as? ExposedBlob)?.inputStream ?: value) {
            is InputStream -> stmt.setInputStream(index, toSetValue, useObjectIdentifier)
            null, is Op.NULL -> stmt.setNull(index, this)
            else -> super.setParameter(stmt, index, toSetValue)
        }
    }

    override fun clone(): IColumnType<ExposedBlob> = BlobColumnType(useObjectIdentifier)
}

internal actual fun basicBinaryValueFromDb(value: Any): ByteArray = when (value) {
    is Blob -> value.binaryStream.use { it.readBytes() }
    is InputStream -> value.use { it.readBytes() }
    is ByteArray -> value
    is String -> value.toByteArray()
    is ByteBuffer -> value.array()
    else -> error("Unexpected value $value of type ${value::class.qualifiedName}")
}

internal actual fun stringValueFromDb(value: Any): String = when (value) {
    is Clob -> value.characterStream.readText()
    is ByteArray -> String(value)
    else -> value.toString()
}

internal actual fun uLongNotNullValueToDb(value: ULong): Any {
    val dialect = currentDialect
    return when {
        // PostgreSQLNG does not throw `out of range` error, so it's handled here to prevent storing invalid values
        dialect is PostgreSQLNGDialect -> {
            value.takeIf { it >= 0uL && it <= Long.MAX_VALUE.toULong() }?.toLong()
                ?: error("Value out of range: $value")
        }

        dialect is PostgreSQLDialect -> BigInteger(value.toString())
        // Long is also an accepted mapping, but this would require handling as above for Oor errors
        dialect is H2Dialect -> BigDecimal(value.toString())

        else -> value.toString()
    }
}

internal actual fun arrayConvertValuesFromDb(value: Any): Array<*>? {
    return when (value) {
        is Array<*> -> value
        is java.sql.Array -> value.array as Array<*>
        else -> null
    }
}

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun <T : Any> resolvePlatformColumnType(klass: KClass<T>): ColumnType<T>? = when (klass) {
    BigDecimal::class -> DecimalColumnType.INSTANCE
    else -> null
} as ColumnType<T>?

internal actual typealias PlatformBlobColumnType = BlobColumnType
