package com.library.loans.domain

import java.time.LocalDate

data class Patron(
        var id: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var address: String = "",
        var phone: String = "",
        var patronSince: LocalDate = LocalDate.now(),
        var expirationDate: LocalDate = LocalDate.now(),
        var loans: List<Loan> = emptyList()
)
