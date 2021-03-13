package com.n26.usecases.transaction.gateways

import com.n26.entities.transaction.Transaction
import java.time.LocalDateTime

interface TimeProviderGateway {

    fun now(): LocalDateTime
}