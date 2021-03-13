package com.n26.usecases.transaction.exceptions

class TransactionHasFutureDateException(msg: String, throwable: Throwable? = null) : Exception(msg, throwable)