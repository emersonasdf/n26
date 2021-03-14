package com.n26.usecases.transaction

import com.n26.entities.transaction.Statistics
import com.n26.usecases.transaction.gateways.TimeProviderGateway
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class StatisticService(
    private val repository: TransactionRepositoryGateway,
    private val timeProvider: TimeProviderGateway
) {

    @Value("\${transaction.expire.time.in.seconds}")
    lateinit var expireTimeInSeconds: String

    private val decimalRoundStrategy = RoundingMode.HALF_UP
    private val decimalScale = 2

    fun get(): Statistics {
        val now = timeProvider.now()
        val nonExpiredTime = now.minusSeconds(getExpireTime())

        val data = repository.getBetween(nonExpiredTime, now)
        if (data.isEmpty()) {
            return Statistics(
                sum = BigDecimal.ZERO,
                avg = BigDecimal.ZERO,
                max = BigDecimal.ZERO,
                min = BigDecimal.ZERO,
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

        val avg = sum.divide(BigDecimal(count), decimalScale, decimalRoundStrategy)

        return Statistics(
            sum = sum.normalize(),
            avg = avg.normalize(),
            max = max.normalize(),
            min = min.normalize(),
            count = count,
        )
    }

    private fun BigDecimal.normalize(): BigDecimal = this.setScale(decimalScale, decimalRoundStrategy)

//    FIXME: TODO: DRY here
    private fun getExpireTime(): Long = expireTimeInSeconds.toLong()
}
