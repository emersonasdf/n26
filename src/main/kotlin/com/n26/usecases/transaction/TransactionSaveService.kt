package com.n26.usecases.transaction

import com.n26.entities.transaction.Transaction
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import com.n26.usecases.transaction.models.TransactionCreationModel
import org.springframework.stereotype.Service

@Service
class TransactionSaveService(
    private val repository: TransactionRepositoryGateway
) {

    fun add(transactionModel: TransactionCreationModel) {
        val transaction = Transaction(
            amount = transactionModel.amount,
            timestamp = transactionModel.timestamp
        )

        repository.create(transaction)
    }
}