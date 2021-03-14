package com.n26.usecases.transaction

import com.n26.usecases.transaction.gateways.TimeProviderGateway
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime
import java.time.ZoneOffset

class TransactionDeleteServiceTest {
    private val mockRepository: TransactionRepositoryGateway = mock()
    private val mockTimeProvider: TimeProviderGateway = mock()
    private val service = TransactionDeleteService(mockRepository, mockTimeProvider)
    private val now = LocalDateTime.now(ZoneOffset.UTC)

    @Before
    fun setup() {
        ReflectionTestUtils.setField(service, "expireTimeInSeconds", "60")
        whenever(mockTimeProvider.now()).thenReturn(now)
    }

    @Test
    fun `deleteExpired should call repository deleteBefore with correct datetime`() {
        val expectedDateTime = now.minusSeconds(60)
        service.deleteExpired()
        verify(mockRepository).deleteBefore(expectedDateTime)
    }

    @Test
    fun `reset should call repository reset function`() {
        service.reset()
        verify(mockRepository).reset()
    }
}
