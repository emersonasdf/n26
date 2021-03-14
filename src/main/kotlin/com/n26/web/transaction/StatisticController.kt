package com.n26.web.transaction

import com.n26.usecases.transaction.StatisticService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.text.DecimalFormat

@RestController
class StatisticController(
    private val service: StatisticService
) {

    private val formatter = DecimalFormat("0.00")

    @GetMapping("/statistics")
    @ResponseStatus(HttpStatus.OK)
    fun get(): StatisticsResponse {
        val response =  service.get()
        return StatisticsResponse(
            sum = formatter.format(response.sum),
            avg = formatter.format(response.avg),
            max = formatter.format(response.max),
            min = formatter.format(response.min),
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
