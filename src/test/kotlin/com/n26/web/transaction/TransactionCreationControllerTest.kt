package com.n26.web.transaction

import com.fasterxml.jackson.databind.ObjectMapper
import com.n26.usecases.transaction.TransactionSaveService
import com.n26.usecases.transaction.exceptions.TransactionHasFutureDateException
import com.n26.usecases.transaction.exceptions.TransactionIsTooOldToSaveException
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertNotNull


@RunWith(SpringRunner::class)
@WebMvcTest(TransactionCreationController::class)
class TransactionCreationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: TransactionSaveService

    @Test
    fun `context should be loaded`() {
        assertNotNull(mockMvc)
    }

    @Test
    fun `should return 201 status code when payload is valid`() {
        mockMvc.perform(generateRequest())
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `should return 204 when transaction is too old`() {
        whenever(service.add(any())).thenThrow(TransactionIsTooOldToSaveException(""))
        mockMvc.perform(generateRequest())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should return 400 if JSON is invalid`() {
        val invalidJson = """
            {
                "amount": "10.0",
                "timestamp
            }
        """.trimIndent()

        mockMvc.perform(getBaseRequestBuilder().content(invalidJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 422 if field is not parsable`() {
        val invalidTimestamp = """
            {
                "amount": "10.0",
                "timestamp": "14/03/2021"
            }
        """.trimIndent()

        mockMvc.perform(getBaseRequestBuilder().content(invalidTimestamp))
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    @Test
    fun `should return 422 when transaction is in the future`() {
        whenever(service.add(any())).thenThrow(TransactionHasFutureDateException(""))
        mockMvc.perform(generateRequest())
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }

    private fun getBaseRequestBuilder() = MockMvcRequestBuilders.post("/transactions")
        .contentType(MediaType.APPLICATION_JSON)

    private fun generateRequest() = getBaseRequestBuilder()
        .content(asJsonString(mapOf(
            "amount" to BigDecimal(10).toString(),
            "timestamp" to Instant.now().toString()
        )))

    private fun asJsonString(obj: Any): String {
        try {
            return ObjectMapper().writeValueAsString(obj);
        } catch (e: Exception) {
            throw RuntimeException (e);
        }
    }
}
