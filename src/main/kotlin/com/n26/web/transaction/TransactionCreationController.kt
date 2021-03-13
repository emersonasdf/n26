package com.n26.web.transaction

import com.n26.usecases.transaction.TransactionSaveService
import com.n26.usecases.transaction.exceptions.TransactionHasFutureDateException
import com.n26.usecases.transaction.exceptions.TransactionIsTooOldToSaveException
import com.n26.usecases.transaction.models.TransactionCreationModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDateTime

@RestController
class TransactionCreationController(
    private val service: TransactionSaveService
) {

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody request: TransactionCreationRequest): ResponseEntity<Unit> {
        val creationModel = TransactionCreationModel(
            amount = request.amount,
            timestamp = request.timestamp
        )

        return try {
            service.add(creationModel)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: TransactionIsTooOldToSaveException) {
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: TransactionHasFutureDateException) {
            ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY)
        }
    }

    data class TransactionCreationRequest (
        val amount: BigDecimal,
        val timestamp: LocalDateTime
    )
}
