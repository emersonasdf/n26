package com.n26.usecases.transaction.exceptions

class TransactionIsTooOldToSaveException(msg: String, throwable: Throwable? = null) : Exception(msg, throwable)