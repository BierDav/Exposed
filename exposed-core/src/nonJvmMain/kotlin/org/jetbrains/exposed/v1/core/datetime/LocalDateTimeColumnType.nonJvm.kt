package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal actual fun localDateTimeValueFromDB(value: Any): LocalDateTime? = when (value) {
    is LocalDateTime -> value
    is LocalDate -> value.atTime(LocalTime(0, 0))
    is Instant -> value.toLocalDateTime(TimeZone.currentSystemDefault())
    is Int -> longToLocalDateTime(value.toLong())
    is Long -> longToLocalDateTime(value)
    is String -> parseLocalDateTime(value)

    else -> localDateTimeValueFromDB(value.toString())
}
