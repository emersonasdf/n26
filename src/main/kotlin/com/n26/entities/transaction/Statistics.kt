package com.n26.entities.transaction

import java.math.BigDecimal

data class Statistics(
    val sum: BigDecimal?,
    val avg: BigDecimal?,
    val max: BigDecimal?,
    val min: BigDecimal?,
    val count: Int
)
