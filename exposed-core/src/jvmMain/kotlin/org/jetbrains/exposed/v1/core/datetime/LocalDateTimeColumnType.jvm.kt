package org.jetbrains.exposed.v1.core.datetime

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.text.toLong
import kotlin.time.toKotlinInstant

@Suppress(names = ["MagicNumber"])
internal actual fun localDateTimeValueFromDB(value: Any): LocalDateTime? = when (value) {
    is LocalDateTime -> value
    is java.sql.Date -> longToLocalDateTime(value.time)
    is java.sql.Timestamp -> longToLocalDateTime(value.time / 1000, value.nanos.toLong())
    is Int -> longToLocalDateTime(value.toLong())
    is Long -> longToLocalDateTime(value)
    is String -> parseLocalDateTime(value)
    is java.time.OffsetDateTime -> {
        // It looks like there is no direct coversion between OffsetDateTime and anything from kotlin datetime modules,
        //  so here conversion happens via java.time.Instant
        value.toInstant()
            .toKotlinInstant()
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    else -> localDateTimeValueFromDB(value.toString())
}
