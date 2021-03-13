package com.n26.usecases.transaction.gateways

import com.n26.entities.transaction.Transaction
import java.time.LocalDateTime

interface TransactionRepositoryGateway {

    fun create(transaction: Transaction)

    fun getBetween(from: LocalDateTime, to: LocalDateTime): List<Transaction>
}