package com.n26.usecases.transaction.models

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionCreationModel (
    val amount: BigDecimal,
    val timestamp: LocalDateTime
)