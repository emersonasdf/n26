package com.n26.persistence.transaction

import com.n26.entities.transaction.Transaction
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.TreeMap

@Repository
class TransactionTable: TransactionRepositoryGateway {

    private val zone = ZoneOffset.UTC
    private val data = TreeMap<Long, List<Transaction>>()

    @Synchronized
    override fun create(transaction: Transaction) {
        val epoch = transaction.timestamp.toEpochMillis()

        val currentIndexData = data[epoch] as? ArrayList<Transaction>
        if (currentIndexData == null) {
            data[epoch] = mutableListOf(transaction)
        } else {
            currentIndexData.add(transaction)
        }
    }

    @Synchronized
    fun getAll(): List<Transaction> {
        return data.flatMap { it.value }
    }

    @Synchronized
    override fun getBetween(from: LocalDateTime, to: LocalDateTime): List<Transaction> {
        return data
            .tailMap(from.toEpochMillis())
            .headMap(to.toEpochMillis())
            .flatMap { it.value }
    }

    @Synchronized
    override fun deleteBefore(time: LocalDateTime) {
        data
            .headMap(time.toEpochMillis())
            .keys
            .toList()
            .forEach(data::remove)
    }

    @Synchronized
    override fun reset() {
        data.clear()
    }

    private fun LocalDateTime.toEpochMillis(): Long {
        return this.toInstant(zone).toEpochMilli()
    }
}
