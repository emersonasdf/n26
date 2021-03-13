package com.n26.usecases.transaction

import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import org.springframework.stereotype.Service

@Service
class TransactionResetService(
    private val repository: TransactionRepositoryGateway
) {

    fun reset() {
        repository.reset()
    }

}