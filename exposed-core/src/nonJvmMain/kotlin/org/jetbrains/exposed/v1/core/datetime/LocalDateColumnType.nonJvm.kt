package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDate
import kotlin.time.Instant

internal actual fun localDateValueFromDB(value: Any): LocalDate = when (value) {
    is LocalDate -> value
    is Instant -> longToLocalDate(value.toEpochMilliseconds())
    is Int -> longToLocalDate(value.toLong())
    is Long -> longToLocalDate(value)
    is String -> LocalDate.parse(value)
    else -> LocalDate.parse(value.toString())
}
