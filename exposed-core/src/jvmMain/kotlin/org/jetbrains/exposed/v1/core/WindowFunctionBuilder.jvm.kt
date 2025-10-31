package org.jetbrains.exposed.v1.core

/**
 * Returns the relative rank of the current row, that is (rank - 1) / (total partition rows - 1).
 * The value thus ranges from 0 to 1 inclusive.
 */
fun percentRank(): PercentRank = PercentRank()

/**
 * Returns the cumulative distribution, that is (number of partition rows preceding or peers with current row) /
 * (total partition rows). The value thus ranges from 1/N to 1.
 */
fun cumeDist(): CumeDist = CumeDist()
