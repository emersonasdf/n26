package com.n26.usecases.transaction

import com.n26.usecases.transaction.gateways.TimeProviderGateway
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TransactionDeleteService(
    private val repository: TransactionRepositoryGateway,
    private val timeProvider: TimeProviderGateway
) {
    @Value("\${transaction.expire.time.in.seconds}")
    lateinit var expireTimeInSeconds: String


    fun deleteExpired() {
        val nonExpiredTime = timeProvider.now().minusSeconds(getExpireTime())
        repository.deleteBefore(nonExpiredTime)
    }

    fun reset() {
        repository.reset()
    }

    //    FIXME: TODO: DRY here
    private fun getExpireTime(): Long {
        return expireTimeInSeconds.toLong()
    }
}
