@file:Suppress("PackageDirectoryMismatch", "InvalidPackageDeclaration")

package org.jetbrains.exposed.v1.exceptions

import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementContext
import org.jetbrains.exposed.v1.core.vendors.DatabaseDialect

/**
 * An exception that provides information about a database access error,
 * within the [contexts] of the executed statements that caused the exception.
 */
expect class ExposedSQLException(
    cause: Throwable?,
    contexts: List<StatementContext>,
    transaction: Transaction
) {
    val contexts: List<StatementContext>
    val transaction: Transaction
}

/**
 * An exception that provides information about an operation that is not supported by
 * the provided [dialect].
 */
class UnsupportedByDialectException(baseMessage: String, val dialect: DatabaseDialect) : UnsupportedOperationException(
    baseMessage + ", dialect: ${dialect.name}."
)

/**
 * DuplicateColumnException is thrown :
 *
 * When you attempt to create a table with multiple columns having the same name.
 * When you replace a column of a table so that you define multiple columns having the same name.
 *
 * @param columnName the duplicated column name
 */
class DuplicateColumnException(columnName: String, tableName: String) : Exception(
    "Duplicate column name \"$columnName\" in table \"$tableName\""
)

/**
 * LongQueryException is thrown:
 *
 * When query running time is greater than value defined in DatabaseConfig.warnLongQueriesDuration
 *
 * @see org.jetbrains.exposed.v1.sql.DatabaseConfig.warnLongQueriesDuration
 */
class LongQueryException : RuntimeException("Long query was executed")

internal fun Transaction.throwUnsupportedException(message: String): Nothing = throw UnsupportedByDialectException(
    message,
    db.dialect
)
