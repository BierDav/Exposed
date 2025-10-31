package org.jetbrains.exposed.v1.core.functions.math

import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.core.vendors.H2Dialect
import org.jetbrains.exposed.v1.core.vendors.OracleDialect
import org.jetbrains.exposed.v1.core.vendors.SQLiteDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialectIfAvailable
import org.jetbrains.exposed.v1.core.vendors.h2Mode

/**
 * Returns the absolute value of a number
 */
class AbsFunction<T : Number?>(expression: ExpressionWithColumnType<T>) : CustomFunction<T>(
    functionName = "ABS",
    columnType = expression.columnType,
    expr = arrayOf(expression)
)

/**
 * Returns the smallest integer value that is >= a number
 */
class CeilingFunction<T : Number?>(expression: ExpressionWithColumnType<T>) : CustomFunction<Long?>(
    functionName = if (
        currentDialectIfAvailable is SQLiteDialect || currentDialectIfAvailable is OracleDialect ||
        currentDialectIfAvailable?.h2Mode == H2Dialect.H2CompatibilityMode.Oracle
    ) {
        "CEIL"
    } else {
        "CEILING"
    },
    columnType = LongColumnType(),
    expr = arrayOf(expression)
)

/**
 * 	Returns the largest integer value that is <= to a number
 */
class FloorFunction<T : Number?>(expression: ExpressionWithColumnType<T>) : CustomFunction<Long?>(
    functionName = "FLOOR",
    columnType = LongColumnType(),
    expr = arrayOf(expression)
)

/**
 * Returns the sign of a number:
 *  -1 - negative number
 *  0 - number is 0
 *  1 - positive number
 */
class SignFunction<T : Number?>(expression: ExpressionWithColumnType<T>) : CustomFunction<Int?>(
    functionName = "SIGN",
    columnType = IntegerColumnType(),
    expr = arrayOf(expression)
)
