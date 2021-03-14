package com.n26.usecases.transaction

import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@EnableScheduling
class TransactionCleanerTaskScheduler(
    private val transactionDeleteService: TransactionDeleteService
) {

    @Scheduled(fixedDelay = 1000)
    fun task() {
        transactionDeleteService.deleteExpired()
    }
}
