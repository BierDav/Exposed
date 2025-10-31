package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Instant

internal actual fun instantValueFromDB(value: Any): Instant = when (value) {
    is Instant -> value
    is String -> parseInstantFromString(value)
    is LocalDateTime -> value.toInstant(TimeZone.currentSystemDefault())
    else -> instantValueFromDB(value.toString())
}
