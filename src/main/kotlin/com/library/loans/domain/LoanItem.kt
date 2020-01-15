package com.library.loans.domain

import java.time.LocalDate

data class LoanItem(
        val isbn: String,
        val title: String,
        val author: String,
        val eligibleForRenewal: Boolean,
        val dueDate: LocalDate
)

