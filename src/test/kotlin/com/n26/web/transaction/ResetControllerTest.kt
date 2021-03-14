package com.n26.web.transaction

import com.n26.usecases.transaction.StatisticService
import com.n26.usecases.transaction.TransactionDeleteService
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
import kotlin.test.assertNotNull


@RunWith(SpringRunner::class)
@WebMvcTest(TransactionResetController::class)
class ResetControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `context should be loaded`() {
        assertNotNull(mockMvc)
    }

    @Test
    fun `should return 204 on reset`() {
        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }
}
