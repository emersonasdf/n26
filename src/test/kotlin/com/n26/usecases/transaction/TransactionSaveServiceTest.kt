package com.n26.usecases.transaction

import com.n26.entities.transaction.Transaction
import com.n26.usecases.transaction.exceptions.TransactionHasFutureDateException
import com.n26.usecases.transaction.exceptions.TransactionIsTooOldToSaveException
import com.n26.usecases.transaction.gateways.TimeProviderGateway
import com.n26.usecases.transaction.gateways.TransactionRepositoryGateway
import com.n26.usecases.transaction.models.TransactionCreationModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset


class TransactionSaveServiceTest {
    private val mockRepository: TransactionRepositoryGateway = mock()
    private val mockTimeProvider: TimeProviderGateway = mock()
    private val service = TransactionSaveService(mockRepository, mockTimeProvider)
    private val now = LocalDateTime.now(ZoneOffset.UTC)

    @Before
    fun setup() {
        ReflectionTestUtils.setField(service, "expireTimeInSeconds", "60")
        whenever(mockTimeProvider.now()).thenReturn(now)
    }

    @Test(expected = TransactionIsTooOldToSaveException::class)
    fun `should raise exception if the transaction is too old`() {
        service.add(
            TransactionCreationModel(BigDecimal.TEN, now.minusSeconds(61))
        )
    }

    @Test(expected = TransactionHasFutureDateException::class)
    fun `should raise exception if the transaction is in the future`() {
        service.add(
            TransactionCreationModel(BigDecimal.TEN, now.plusSeconds(1))
        )
    }

    @Test
    fun `should call repository create with built Transaction object`() {
        val creationModel = TransactionCreationModel(BigDecimal.TEN, now)
        val expectedTransaction = Transaction(creationModel.amount, creationModel.timestamp)

        service.add(creationModel)

        verify(mockRepository).create(expectedTransaction)
    }
}
