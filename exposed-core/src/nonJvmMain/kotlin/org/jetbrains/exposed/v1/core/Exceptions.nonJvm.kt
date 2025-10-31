package org.jetbrains.exposed.v1.exceptions

import org.jetbrains.exposed.v1.core.AbstractQuery
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementContext
import org.jetbrains.exposed.v1.core.statements.expandArgs

actual class ExposedSQLException actual constructor(
    cause: Throwable?,
    actual val contexts: List<StatementContext>,
    actual val transaction: Transaction
) : Exception(cause) {
    fun causedByQueries(): List<String> = contexts.map {
        try {
            if (transaction.debug) {
                it.expandArgs(transaction)
            } else {
                it.sql(transaction)
            }
        } catch (_: Throwable) {
            try {
                (it.statement as? AbstractQuery<*>)?.prepareSQL(QueryBuilder(!transaction.debug))
            } catch (_: Throwable) {
                null
            } ?: "Failed on expanding args for ${it.statement.type}: ${it.statement}"
        }
    }

    override fun toString() = "${super.toString()}\nSQL: ${causedByQueries()}"
}
