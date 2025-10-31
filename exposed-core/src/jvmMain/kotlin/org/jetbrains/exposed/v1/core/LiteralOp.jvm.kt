package org.jetbrains.exposed.v1.core

import java.math.BigDecimal

/** Returns the specified [value] as a decimal literal. */
fun decimalLiteral(value: BigDecimal): LiteralOp<BigDecimal> = LiteralOp(DecimalColumnType(value.precision(), value.scale()), value)
