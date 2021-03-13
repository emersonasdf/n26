package com.n26.web.transaction

import com.n26.usecases.transaction.TransactionResetService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionResetController(
    private val service: TransactionResetService
) {

    @DeleteMapping("/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete() {
        service.reset()
    }

    data class StatisticsResponse(
        val sum: String?,
        val avg: String?,
        val max: String?,
        val min: String?,
        val count: Int
    )
}
