package org.jetbrains.exposed.v1.core.datetime

import java.sql.Timestamp
import java.time.ZoneId
import kotlin.time.Instant

actual fun instantValueFromDB(value: Any): Instant = when (value) {
    is Timestamp -> Instant.fromEpochSeconds(value.time / 1000, value.nanos)
    is String -> parseInstantFromString(value)
    is java.time.LocalDateTime -> {
        value.atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
            .let { Instant.fromEpochMilliseconds(it) }
    }
    else -> instantValueFromDB(value.toString())
}
