package com.n26.usecases.transaction

import com.n26.entities.transaction.Statistics
import com.n26.entities.transaction.Transaction
import com.n26.usecases.transaction.gateways.TimeProviderGateway
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals

class StatisticServiceTest {

    private val mockRepository: TransactionRepositoryGateway = mock()
    private val mockTimeProvider: TimeProviderGateway = mock()
    private val service = StatisticService(mockRepository, mockTimeProvider)
    private val now = LocalDateTime.now(ZoneOffset.UTC)
    private val fromDate = now.minusSeconds(60)
    private val defaultTransactionList = listOf(
        Transaction(BigDecimal(10.47), now),
        Transaction(BigDecimal(20.11), now),
        Transaction(BigDecimal(40.20), now)
    )

    @Before
    fun setup() {
        ReflectionTestUtils.setField(service, "expireTimeInSeconds", "60")
        whenever(mockTimeProvider.now()).thenReturn(now)
    }

    @Test
    fun `should call repository getBetween with correct parameters`() {
        service.get()
        verify(mockRepository).getBetween(fromDate, now)
    }

    @Test
    fun `should return correct statistics if there is no recent transactions`() {
        val response = service.get()
        val expected = Statistics(
            sum = BigDecimal.ZERO,
            avg = BigDecimal.ZERO,
            max = BigDecimal.ZERO,
            min = BigDecimal.ZERO,
            count = 0,
        )

        assertEquals(expected, response)
    }

    @Test
    fun `normalize should return with scale of two`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(defaultTransactionList)

        val response = service.get()
        assertEquals(response.sum.scale(), 2)
        assertEquals(response.avg.scale(), 2)
        assertEquals(response.max.scale(), 2)
        assertEquals(response.min.scale(), 2)
    }

    @Test
    fun `normalize should round down if half-down`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(listOf(
            Transaction(BigDecimal(10.4700000), now),
            Transaction(BigDecimal(20.1111111), now)
        ))

        assertEquals(BigDecimal(30.58).normalize(), service.get().sum)
    }

    @Test
    fun `normalize should round up if half-up`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(listOf(
            Transaction(BigDecimal(10.4700000), now),
            Transaction(BigDecimal(20.1111111), now)
        ))

        assertEquals(BigDecimal(30.58).normalize(), service.get().sum)
    }

    @Test
    fun `should return correct sum`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(defaultTransactionList)
        assertEquals(BigDecimal(70.78).normalize(), service.get().sum)
    }

    @Test
    fun `should return correct avg`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(defaultTransactionList)
        assertEquals(BigDecimal(23.59).normalize(), service.get().avg)
    }

    @Test
    fun `should return correct max`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(defaultTransactionList)
        assertEquals(BigDecimal(40.20).normalize(), service.get().max)
    }


    @Test
    fun `should return correct min`() {
        whenever(mockRepository.getBetween(any(), any())).thenReturn(defaultTransactionList)
        assertEquals(BigDecimal(10.47).normalize(), service.get().min)
    }


    private fun BigDecimal.normalize(): BigDecimal =this.setScale(2, RoundingMode.HALF_UP)
}
