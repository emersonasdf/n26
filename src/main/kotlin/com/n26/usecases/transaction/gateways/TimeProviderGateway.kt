package com.n26.usecases.transaction.gateways

import java.time.LocalDateTime

interface TimeProviderGateway {

    fun now(): LocalDateTime
}
