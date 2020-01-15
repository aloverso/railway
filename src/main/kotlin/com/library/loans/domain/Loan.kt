package com.library.loans.domain

import java.time.LocalDate

data class Loan(
        val id: String = "",
        val checkoutDate: LocalDate = LocalDate.now(),
        val items: List<String> = emptyList()
)