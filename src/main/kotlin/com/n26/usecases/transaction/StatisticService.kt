package com.n26.usecases.transaction

import com.n26.entities.transaction.Statistics
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class StatisticService(
    private val repository: TransactionRepositoryGateway
) {

    fun get(): Statistics {
        val now = LocalDateTime.now()
        val oneMinuteAgo = now.minusDays(1)

        val data = repository.getBetween(oneMinuteAgo, now)
        if (data.isEmpty()) {
            return Statistics(
                sum = null,
                avg = null,
                max = null,
                min = null,
                count = 0,
            )
        }

        val count = data.size
        var sum = BigDecimal.ZERO
        var min = data.first().amount
        var max = data.first().amount

        data.forEach {
            sum += it.amount
            min = if (it.amount < min) it.amount else min
            max = if (it.amount > max) it.amount else max
        }

        val avg = if (count > 0) sum.divide(BigDecimal(count)) else null

        return Statistics(
            sum = sum,
            avg = avg,
            max = max,
            min = min,
            count = count,
        )
    }
}