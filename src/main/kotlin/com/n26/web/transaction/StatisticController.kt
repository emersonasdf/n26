package com.n26.web.transaction

import com.n26.entities.transaction.Statistics
import com.n26.usecases.transaction.StatisticService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
class StatisticController(
    private val service: StatisticService
) {

    @GetMapping("/statistics")
    @ResponseStatus(HttpStatus.OK)
    fun get(): StatisticsResponse {
        val response =  service.get()

        return StatisticsResponse(
            sum = response.sum?.toString(),
            avg = response.avg?.toString(),
            max = response.max?.toString(),
            min = response.min?.toString(),
            count = response.count,
        )
    }

    data class StatisticsResponse(
        val sum: String?,
        val avg: String?,
        val max: String?,
        val min: String?,
        val count: Int
    )
}
