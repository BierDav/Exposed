package org.jetbrains.exposed.v1.core

// General-Purpose Aggregate Functions

/** Returns the average (arithmetic mean) value of this expression across all non-null input values, or `null` if there are no non-null values. */
fun <T : Comparable<T>, S : T?> ExpressionWithColumnType<S>.avg(scale: Int = 2): Avg<T, S> = Avg<T, S>(this, scale)


// Aggregate Functions for Statistics

/**
 * Returns the population standard deviation of the non-null input values, or `null` if there are no non-null values.
 *
 * @param scale The scale of the decimal column expression returned.
 */
fun <T : Any?> ExpressionWithColumnType<T>.stdDevPop(scale: Int = 2): StdDevPop<T> = StdDevPop(this, scale)

/**
 * Returns the sample standard deviation of the non-null input values, or `null` if there are no non-null values.
 *
 * @param scale The scale of the decimal column expression returned.
 */
fun <T : Any?> ExpressionWithColumnType<T>.stdDevSamp(scale: Int = 2): StdDevSamp<T> = StdDevSamp(this, scale)

/**
 * Returns the population variance of the non-null input values (square of the population standard deviation), or `null` if there are no non-null values.
 *
 * @param scale The scale of the decimal column expression returned.
 */
fun <T : Any?> ExpressionWithColumnType<T>.varPop(scale: Int = 2): VarPop<T> = VarPop(this, scale)

/**
 * Returns the sample variance of the non-null input values (square of the sample standard deviation), or `null` if there are no non-null values.
 *
 * @param scale The scale of the decimal column expression returned.
 */
fun <T : Any?> ExpressionWithColumnType<T>.varSamp(scale: Int = 2): VarSamp<T> = VarSamp(this, scale)
