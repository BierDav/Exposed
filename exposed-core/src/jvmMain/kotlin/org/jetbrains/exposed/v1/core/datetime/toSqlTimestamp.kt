package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

internal fun LocalDateTime.toSqlTimestamp(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toInstant(timeZone).let {
    java.sql.Timestamp(it.toEpochMilliseconds())
        .apply { this.nanos = it.nanosecondsOfSecond }
}
