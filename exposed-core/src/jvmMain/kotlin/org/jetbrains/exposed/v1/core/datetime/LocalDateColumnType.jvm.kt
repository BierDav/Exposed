package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDate
import java.util.Date

internal actual fun localDateValueFromDB(value: Any): LocalDate = when (value) {
    is LocalDate -> value
    is Date -> longToLocalDate(value.time)
    is Int -> longToLocalDate(value.toLong())
    is Long -> longToLocalDate(value)
    is String -> LocalDate.parse(value)
    else -> LocalDate.parse(value.toString())
}
