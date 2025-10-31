package org.jetbrains.exposed.v1.core

import org.jetbrains.exposed.v1.core.statements.api.ExposedBlob
import java.math.BigDecimal

/** Returns the specified [value] as a decimal query parameter. */
fun decimalParam(value: BigDecimal): Expression<BigDecimal> = QueryParameter(value, DecimalColumnType(value.precision(), value.scale()))

/**
 * Returns the specified [value] as a blob query parameter.
 *
 * Set [useObjectIdentifier] to `true` if the parameter should be processed using an OID column instead of a
 * BYTEA column. This is only supported by PostgreSQL databases.
 */
fun blobParam(value: ExposedBlob, useObjectIdentifier: Boolean = false): Expression<ExposedBlob> =
    QueryParameter(value, BlobColumnType(useObjectIdentifier))
