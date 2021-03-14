package com.n26.web.transaction

import com.n26.entities.transaction.Statistics
import com.n26.usecases.transaction.StatisticService
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import kotlin.test.assertNotNull


@RunWith(SpringRunner::class)
@WebMvcTest(StatisticController::class)
class StatisticControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: StatisticService

    private val defaultServiceResponse = Statistics(
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        0)

    @Test
    fun `context should be loaded`() {
        assertNotNull(mockMvc)
    }

    @Test
    fun `should receive 200`() {
        whenever(service.get()).thenReturn(defaultServiceResponse)
        mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `should format correctly`() {
        whenever(service.get()).thenReturn(defaultServiceResponse)
        val expectedResponse = """{"sum": "0.00","avg": "0.00","max": "0.00","min": "0.00","count": 0}"""

        mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
    }

    @Test
    fun `should format correctly with data`() {
        whenever(service.get()).thenReturn(Statistics(
            sum = BigDecimal(39128312.1010021012),
            avg = BigDecimal(10.1210021012),
            max = BigDecimal(300.9010021012),
            min = BigDecimal(9.2110021012),
            count = 10210,
        ))

        val expectedResponse = """{"sum": "39128312.10","avg": "10.12","max": "300.90","min": "9.21","count":10210}"""

        mockMvc.perform(MockMvcRequestBuilders.get("/statistics"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(expectedResponse))
    }
}
