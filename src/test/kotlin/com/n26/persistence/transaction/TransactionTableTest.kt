package com.n26.persistence.transaction

import com.n26.entities.transaction.Transaction
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TransactionTableTest {

    private val repository = TransactionTable()

    private val now = LocalDateTime.now(ZoneOffset.UTC)
    private val defaultTransactionList = listOf(
        Transaction(BigDecimal(10.47), now.minusSeconds(11)),
        Transaction(BigDecimal(20.11), now.minusSeconds(10)),
        Transaction(BigDecimal(40.20), now)
    )

    @Before
    fun beforeEach() {
        repository.reset()
    }

    @Test
    fun `create should persist transaction`() {
        repository.create(defaultTransactionList.first())
        assertEquals(listOf(defaultTransactionList.first()), repository.getAll())
    }

    @Test
    fun `getAll should return empty list when empty repository`() {
        assertEquals(emptyList(), repository.getAll())
    }


    @Test
    fun `getAll should return all transactions in repository`() {
        createDefaultTransactions()
        assertEquals(defaultTransactionList, repository.getAll())
    }

    @Test
    fun `getBetween should return inclusive 'from' date parameter`() {
        val shouldReturn = Transaction(BigDecimal(10.39), now.minusSeconds(10))
        val shouldNotReturn = Transaction(BigDecimal(10.39), now.minusSeconds(10).minusNanos(1))
        listOf(shouldNotReturn, shouldReturn).forEach(repository::create)

        val response = repository.getBetween(now.minusSeconds(10), now)
        assertEquals(1, response.size)
        assert(shouldReturn in response)
        assert(shouldNotReturn !in response)
    }

    @Test
    fun `getBetween should return exclusive 'to' date parameter`() {
        val shouldReturn = Transaction(BigDecimal(10.39), now.minusNanos(1))
        val shouldNotReturn = listOf(
            Transaction(BigDecimal(10.39), now),
            Transaction(BigDecimal(10.39), now.plusNanos(1))
        )

        listOf(shouldReturn, shouldNotReturn[0], shouldNotReturn[1]).forEach(repository::create)

        val response = repository.getBetween(now.minusSeconds(10), now)
        assertEquals(1, response.size)
        assert(shouldReturn in response)
        assertFalse(shouldNotReturn.any { it in response})
    }

    @Test
    fun `getBetween should return empty list when empty repository`() {
        assertEquals(emptyList(), repository.getBetween(now.minusHours(10), now))
    }

    @Test
    fun `removeBefore should delete with exclusive 'time' param`() {
        createDefaultTransactions()
        assertEquals(defaultTransactionList.size, repository.getAll().size)

        repository.deleteBefore(now)
        assertEquals(1, repository.getAll().size)
    }

    @Test
    fun `removeBefore should not delete transactions after 'time' param`() {
        createDefaultTransactions()
        repository.create(Transaction(BigDecimal.TEN, now.plusNanos(1)))
        assertEquals(defaultTransactionList.size + 1, repository.getAll().size)

        repository.deleteBefore(now)
        assertEquals(2, repository.getAll().size)
    }

    @Test
    fun `should remove all data after reset`() {
        createDefaultTransactions()
        assertEquals(defaultTransactionList, repository.getAll())
        repository.reset()
        assertEquals(emptyList(), repository.getAll())
    }

    private fun createDefaultTransactions() {
        defaultTransactionList.forEach(repository::create)
    }


}
