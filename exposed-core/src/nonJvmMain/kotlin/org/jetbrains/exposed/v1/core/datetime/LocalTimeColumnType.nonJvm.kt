package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toLocalTime
import kotlin.time.Instant

internal actual fun localTimeValueFromDB(value: Any): LocalTime = when (value) {
    is LocalTime -> value
    is Instant -> value.toLocalDateTime(TimeZone.currentSystemDefault()).time
    is Int -> longToLocalTime(value.toLong())
    is Long -> longToLocalTime(value)
    is String -> LocalTime.parse(value, LocalTime.Formats.ISO)
    else -> localTimeValueFromDB(value.toString())
}
