package org.jetbrains.exposed.v1.core

import org.jetbrains.exposed.v1.core.vendors.H2Dialect
import org.jetbrains.exposed.v1.core.vendors.H2FunctionProvider
import org.jetbrains.exposed.v1.core.vendors.currentDialect
import org.jetbrains.exposed.v1.core.vendors.h2Mode
import java.math.BigDecimal


// Mathematical Functions
/**
 * Represents an SQL function that returns a random value in the range 0.0 <= x < 1.0, using the specified [seed].
 *
 * **Note:** Some vendors generate values outside this range, or ignore the given seed, check the documentation.
 */
class Random(
    /** Returns the seed. */
    val seed: Int? = null
) : Function<BigDecimal>(DecimalColumnType(precision = 38, scale = 20)) {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder {
        val functionProvider = when (currentDialect.h2Mode) {
            H2Dialect.H2CompatibilityMode.Oracle, H2Dialect.H2CompatibilityMode.SQLServer -> H2FunctionProvider
            else -> currentDialect.functionProvider
        }
        +functionProvider.random(seed)
    }
}

// General-Purpose Aggregate Functions


/**
 * Represents an SQL function that returns the average (arithmetic mean) of all non-null input values, or `null` if there are no non-null values.
 */
class Avg<T : Comparable<T>, S : T?>(
    /** Returns the expression from which the average is calculated. */
    val expr: Expression<S>,
    scale: Int
) : Function<BigDecimal?>(DecimalColumnType(Int.MAX_VALUE, scale)), WindowFunction<BigDecimal?> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder { append("AVG(", expr, ")") }

    override fun over(): WindowFunctionDefinition<BigDecimal?> {
        return WindowFunctionDefinition(columnType, this)
    }
}



// Aggregate Functions for Statistics

/**
 * Represents an SQL function that returns the population standard deviation of the non-null input values,
 * or `null` if there are no non-null values.
 */
class StdDevPop<T>(
    /** Returns the expression from which the population standard deviation is calculated. */
    val expression: Expression<T>,
    scale: Int
) : Function<BigDecimal?>(DecimalColumnType(Int.MAX_VALUE, scale)), WindowFunction<BigDecimal?> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder {
            val functionProvider = when (currentDialect.h2Mode) {
                H2Dialect.H2CompatibilityMode.SQLServer -> H2FunctionProvider
                else -> currentDialect.functionProvider
            }
            functionProvider.stdDevPop(expression, this)
        }
    }

    override fun over(): WindowFunctionDefinition<BigDecimal?> {
        return WindowFunctionDefinition(columnType, this)
    }
}

/**
 * Represents an SQL function that returns the sample standard deviation of the non-null input values,
 * or `null` if there are no non-null values.
 */
class StdDevSamp<T>(
    /** Returns the expression from which the sample standard deviation is calculated. */
    val expression: Expression<T>,
    scale: Int
) : Function<BigDecimal?>(DecimalColumnType(Int.MAX_VALUE, scale)), WindowFunction<BigDecimal?> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder {
            val functionProvider = when (currentDialect.h2Mode) {
                H2Dialect.H2CompatibilityMode.SQLServer -> H2FunctionProvider
                else -> currentDialect.functionProvider
            }
            functionProvider.stdDevSamp(expression, this)
        }
    }

    override fun over(): WindowFunctionDefinition<BigDecimal?> {
        return WindowFunctionDefinition(columnType, this)
    }
}

/**
 * Represents an SQL function that returns the population variance of the non-null input values (square of the population standard deviation),
 * or `null` if there are no non-null values.
 */
class VarPop<T>(
    /** Returns the expression from which the population variance is calculated. */
    val expression: Expression<T>,
    scale: Int
) : Function<BigDecimal?>(DecimalColumnType(Int.MAX_VALUE, scale)), WindowFunction<BigDecimal?> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder {
            val functionProvider = when (currentDialect.h2Mode) {
                H2Dialect.H2CompatibilityMode.SQLServer -> H2FunctionProvider
                else -> currentDialect.functionProvider
            }
            functionProvider.varPop(expression, this)
        }
    }

    override fun over(): WindowFunctionDefinition<BigDecimal?> {
        return WindowFunctionDefinition(columnType, this)
    }
}

/**
 * Represents an SQL function that returns the sample variance of the non-null input values (square of the sample standard deviation),
 * or `null` if there are no non-null values.
 */
class VarSamp<T>(
    /** Returns the expression from which the sample variance is calculated. */
    val expression: Expression<T>,
    scale: Int
) : Function<BigDecimal?>(DecimalColumnType(Int.MAX_VALUE, scale)), WindowFunction<BigDecimal?> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        queryBuilder {
            val functionProvider = when (currentDialect.h2Mode) {
                H2Dialect.H2CompatibilityMode.SQLServer -> H2FunctionProvider
                else -> currentDialect.functionProvider
            }
            functionProvider.varSamp(expression, this)
        }
    }

    override fun over(): WindowFunctionDefinition<BigDecimal?> {
        return WindowFunctionDefinition(columnType, this)
    }
}
