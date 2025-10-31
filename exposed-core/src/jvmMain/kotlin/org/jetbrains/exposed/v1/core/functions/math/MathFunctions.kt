package org.jetbrains.exposed.v1.core.functions.math

import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.vendors.H2Dialect
import org.jetbrains.exposed.v1.core.vendors.OracleDialect
import org.jetbrains.exposed.v1.core.vendors.SQLiteDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialectIfAvailable
import org.jetbrains.exposed.v1.core.vendors.h2Mode
import java.math.BigDecimal
import java.math.MathContext


/**
 * 	Returns e raised to the power of a specified number
 */
class ExpFunction<T : Number?>(expression: ExpressionWithColumnType<T>) : CustomFunction<BigDecimal?>(
    functionName = "EXP",
    columnType = DecimalColumnType.INSTANCE,
    expr = arrayOf(expression)
)


/**
 * 	Returns the value of a number raised to the power of another number
 */
class PowerFunction<B : Number?, E : Number?>(
    base: ExpressionWithColumnType<B>,
    exponent: Expression<E>,
    precision: Int = MathContext.DECIMAL64.precision,
    /** Count of decimal digits in the fractional part. */
    scale: Int = 10
) : CustomFunction<BigDecimal?>(
    functionName = "POWER",
    columnType = DecimalColumnType(precision, scale),
    expr = arrayOf(base, exponent)
)

/**
 * 	Rounds a number to a specified number of decimal places
 */
class RoundFunction<T : Number?>(expression: ExpressionWithColumnType<T>, scale: Int) : CustomFunction<BigDecimal?>(
    functionName = "ROUND",
    columnType = DecimalColumnType(MathContext.DECIMAL64.precision, scale).apply { nullable = true },
    expr = arrayOf(expression, intLiteral(scale))
)


/**
 * 	Returns the square root of a number
 */
class SqrtFunction<T : Number?>(expression: ExpressionWithColumnType<T>) : CustomFunction<BigDecimal?>(
    functionName = "SQRT",
    columnType = DecimalColumnType.INSTANCE,
    expr = arrayOf(expression)
)
