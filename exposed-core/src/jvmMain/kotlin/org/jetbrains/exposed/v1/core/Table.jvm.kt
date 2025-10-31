package org.jetbrains.exposed.v1.core

import org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import java.math.BigDecimal

/**
 * Creates a numeric column, with the specified [name], for storing numbers with the specified [precision] and [scale].
 *
 * To store the decimal `123.45`, [precision] would have to be set to 5 (as there are five digits in total) and
 * [scale] to 2 (as there are two digits behind the decimal point).
 *
 * @param name Name of the column.
 * @param precision Total count of significant digits in the whole number, that is, the number of digits to both sides of the decimal point.
 * @param scale Count of decimal digits in the fractional part.
 */
fun Table.decimal(name: String, precision: Int, scale: Int): Column<BigDecimal> = registerColumn(
    name,
    DecimalColumnType(precision, scale)
)


/**
 * Creates a binary column, with the specified [name], for storing BLOBs.
 * If [useObjectIdentifier] is `true`, then the column will use the `OID` type on PostgreSQL
 * for storing large binary objects. The parameter must not be `true` for other databases.
 *
 * @sample org.jetbrains.exposed.v1.tests.shared.types.BlobColumnTypeTests.testBlob
 */
fun Table.blob(name: String, useObjectIdentifier: Boolean = false): Column<ExposedBlob> =
    registerColumn(name, BlobColumnType(useObjectIdentifier))
