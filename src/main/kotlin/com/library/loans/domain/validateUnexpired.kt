package com.library.loans.domain

import com.library.railway.Failure
import com.library.railway.Success
import com.library.railway.TwoTrack
import java.time.LocalDate
import java.time.ZoneOffset

fun validateUnexpired(patron: Patron): TwoTrack<Patron> {
    val utcNow = LocalDate.now(ZoneOffset.UTC)

    if (utcNow >= patron.expirationDate) {
        return Failure(ErrorMessage.PatronshipExpired)
    }

    return Success(patron)
}