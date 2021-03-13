package com.n26.web.transaction

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionCreationController {

    @PostMapping("/transactions")
    fun add(): String {
        return "Test"
    }
}