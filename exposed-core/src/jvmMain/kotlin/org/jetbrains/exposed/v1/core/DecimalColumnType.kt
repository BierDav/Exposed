package org.jetbrains.exposed.v1.core

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Numeric column for storing numbers with the specified [precision] and [scale].
 */
class DecimalColumnType(
    /** Total count of significant digits in the whole number. */
    val precision: Int,
    /** Count of decimal digits in the fractional part. */
    val scale: Int
) : ColumnType<BigDecimal>() {
    override fun sqlType(): String = "DECIMAL($precision, $scale)"

    override fun valueFromDB(value: Any): BigDecimal = when (value) {
        is BigDecimal -> value
        is Double -> {
            if (value.isNaN()) {
                error("Unexpected value of type Double: NaN of ${value::class.qualifiedName}")
            } else {
                value.toBigDecimal()
            }
        }

        is Float -> {
            if (value.isNaN()) {
                error("Unexpected value of type Float: NaN of ${value::class.qualifiedName}")
            } else {
                value.toBigDecimal()
            }
        }

        is Long -> value.toBigDecimal()
        is Int -> value.toBigDecimal()
        is Short -> value.toLong().toBigDecimal()
        else -> error("Unexpected value of type Decimal: $value of ${value::class.qualifiedName}")
    }.setScale(scale, RoundingMode.HALF_EVEN)

    override fun clone(): IColumnType<BigDecimal> = DecimalColumnType(precision, scale)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DecimalColumnType

        if (precision != other.precision) return false
        if (scale != other.scale) return false
        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + precision
        result = 31 * result + scale
        return result
    }

    companion object {
        internal val INSTANCE = DecimalColumnType(MathContext.DECIMAL64.precision, 20)
    }
}
