package org.jetbrains.exposed.v1.core

import java.math.BigDecimal


// Value Operators
/** Changes this integer expression to a [BigDecimal] type. */
fun ExpressionWithColumnType<Int>.intToDecimal(): NoOpConversion<Int, BigDecimal> =
    NoOpConversion(this, DecimalColumnType(precision = 15, scale = 0))
