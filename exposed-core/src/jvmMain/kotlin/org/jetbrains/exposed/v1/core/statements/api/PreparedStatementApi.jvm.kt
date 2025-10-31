package org.jetbrains.exposed.v1.core.statements.api

import org.jetbrains.exposed.v1.core.ArrayColumnType
import org.jetbrains.exposed.v1.core.DecimalColumnType
import java.math.BigDecimal
import java.math.MathContext

internal actual fun getDecimalColumnType(type: String): ArrayColumnType<*,*>? {
    val specs = type.substringAfter("DECIMAL").trim('(', ')')
        .takeUnless { it.isEmpty() }
        ?.split(", ")
        ?.map { it.toIntOrNull() }
    // same default values used in exposed-core DecimalColumnType()
    val precision = specs?.firstOrNull() ?: MathContext.DECIMAL64.precision
    val scale = specs?.lastOrNull() ?: 20
    return ArrayColumnType<BigDecimal, List<BigDecimal>>(DecimalColumnType(precision, scale))
}
