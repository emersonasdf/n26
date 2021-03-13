package com.n26.persistence.transaction

import com.n26.entities.transaction.Transaction
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import org.springframework.stereotype.Repository
import java.time.ZoneOffset

@Repository
class TransactionTable: TransactionRepositoryGateway {

    private val data = HashMap<Long, List<Transaction>>()

    override fun create(transaction: Transaction) {
        val epoch = transaction.timestamp.toEpochSecond(ZoneOffset.UTC)

        val currentIndexData = data[epoch] as? ArrayList<Transaction>
        if (currentIndexData == null) {
            data[epoch] = mutableListOf(transaction)
        } else {
            currentIndexData.add(transaction)
        }
    }
}