package com.n26.entities.transaction

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction (
    val amount: BigDecimal,
    val timestamp: LocalDateTime
)