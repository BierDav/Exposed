package org.jetbrains.exposed.v1.core

import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import org.jetbrains.exposed.v1.core.UuidColumnType.Companion.uuidRegexp
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLNGDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialect
import kotlin.reflect.KClass
import kotlin.uuid.Uuid

internal actual fun uuidValueFromDb(value: Any): Uuid = when {
    value is Uuid -> value
    value is ByteArray -> Uuid.fromByteArray(value)
    value is String && value.matches(uuidRegexp) -> Uuid.parse(value)
    else -> error("Unexpected value of type UUID: $value of ${value::class.qualifiedName}")
}

internal actual fun basicBinaryValueFromDb(value: Any): ByteArray = when (value) {
    is ByteArray -> value
    is String -> value.encodeToByteArray()
    is Buffer -> value.readByteArray()
    else -> error("Unexpected value $value of type ${value::class.qualifiedName}")
}

internal actual fun stringValueFromDb(value: Any): String = when (value) {
    is ByteArray -> value.decodeToString()
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

        else -> value.toString()
    }
}

internal actual fun arrayConvertValuesFromDb(value: Any): Array<*>? = value as? Array<*>
@Suppress("NOTHING_TO_INLINE")
internal actual inline fun <T : Any> resolvePlatformColumnType(klass: KClass<T>): ColumnType<T>? = null

internal actual class PlatformBlobColumnType {
    actual val useObjectIdentifier: Boolean = false
}
