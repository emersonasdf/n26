package com.n26.usecases.transaction.gateways

import com.n26.entities.transaction.Transaction

interface TransactionRepositoryGateway {

    fun create(transaction: Transaction)
}