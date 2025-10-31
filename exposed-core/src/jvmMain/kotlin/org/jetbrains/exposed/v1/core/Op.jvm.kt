package org.jetbrains.exposed.v1.core

import java.math.BigDecimal

fun <T : BigDecimal?, S : T> DivideOp<T, S>.withScale(scale: Int): DivideOp<T, S> {
    val precision = (columnType as DecimalColumnType).precision + scale
    val decimalColumnType = DecimalColumnType(precision, scale)

    val newExpression = (dividend as? LiteralOp<BigDecimal>)?.value?.takeIf { it.scale() == 0 }?.let {
        decimalLiteral(it.setScale(1)) // it is needed to treat dividend as decimal instead of integer in SQL
    } ?: dividend

    return DivideOp(newExpression as Expression<T>, divisor, decimalColumnType as IColumnType<T & Any>)
}
