package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalTime
import kotlinx.datetime.toKotlinLocalTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toLocalTime
import java.sql.Timestamp

internal actual fun localTimeValueFromDB(value: Any): LocalTime = when (value) {
    is LocalTime -> value
    is java.sql.Time -> value.toLocalTime().toKotlinLocalTime()
    is Timestamp -> value.toLocalDateTime().toLocalTime().toKotlinLocalTime()
    is Int -> longToLocalTime(value.toLong())
    is Long -> longToLocalTime(value)
    is String -> LocalTime.parse(value, LocalTime.Formats.ISO)
    else -> localTimeValueFromDB(value.toString())
}
