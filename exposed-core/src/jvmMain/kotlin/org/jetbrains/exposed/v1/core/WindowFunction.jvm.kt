package org.jetbrains.exposed.v1.core

import java.math.BigDecimal

/**
 * Represents an SQL function that returns the relative rank of the current row, that is (rank - 1) /
 * (total partition rows - 1). The value thus ranges from 0 to 1 inclusive.
 * [scale] represents decimal digits count in the fractional part of result.
 */
class PercentRank(private val scale: Int = 2) : WindowFunction<BigDecimal> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder {
        +"PERCENT_RANK()"
    }

    override fun over(): WindowFunctionDefinition<BigDecimal> {
        return WindowFunctionDefinition(DecimalColumnType(Int.MAX_VALUE, scale), this)
    }
}

/**
 * Represents an SQL function that Returns the cumulative distribution, that is (number of partition rows preceding
 * or peers with current row) / (total partition rows). The value thus ranges from 1/N to 1.
 * [scale] represents decimal digits count in the fractional part of result.
 */
class CumeDist(private val scale: Int = 2) : WindowFunction<BigDecimal> {
    override fun toQueryBuilder(queryBuilder: QueryBuilder): Unit = queryBuilder {
        +"CUME_DIST()"
    }

    override fun over(): WindowFunctionDefinition<BigDecimal> {
        return WindowFunctionDefinition(DecimalColumnType(Int.MAX_VALUE, scale), this)
    }
}
