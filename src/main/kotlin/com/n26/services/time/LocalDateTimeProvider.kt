package com.n26.services.time

import com.n26.usecases.transaction.gateways.TimeProviderGateway
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class LocalDateTimeProvider: TimeProviderGateway {

    override fun now() = LocalDateTime.now(ZoneOffset.UTC)
}