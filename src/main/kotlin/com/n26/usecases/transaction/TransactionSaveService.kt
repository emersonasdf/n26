package com.n26.usecases.transaction

import com.n26.entities.transaction.Transaction
import com.n26.usecases.transaction.exceptions.TransactionHasFutureDateException
import com.n26.usecases.transaction.exceptions.TransactionIsTooOldToSaveException
import com.n26.usecases.transaction.gateways.TimeProviderGateway
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import com.n26.usecases.transaction.models.TransactionCreationModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class TransactionSaveService(
    private val repository: TransactionRepositoryGateway,
    private val timeProvider: TimeProviderGateway
) {

    @Value("\${transaction.expire.time.in.seconds}")
    lateinit var expireTimeInSeconds: String

    @Throws(
        TransactionIsTooOldToSaveException::class,
        TransactionHasFutureDateException::class
    )
    fun add(transactionModel: TransactionCreationModel) {
        val now = timeProvider.now()
        if (now.minusSeconds(getExpireTime()) > transactionModel.timestamp)
            throw TransactionIsTooOldToSaveException("Transaction is too old to save")
        else if (transactionModel.timestamp > now) {
            throw TransactionHasFutureDateException("Transaction cannot have a future date")
        }

        val transaction = Transaction(
            amount = transactionModel.amount,
            timestamp = transactionModel.timestamp
        )

        repository.create(transaction)
    }

    private fun getExpireTime(): Long {
        return expireTimeInSeconds.toLong()
    }
}
