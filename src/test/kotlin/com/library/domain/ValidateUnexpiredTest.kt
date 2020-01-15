package com.library.domain

import com.library.assertFailure
import com.library.assertSuccess
import com.library.buildPatron
import com.library.loans.domain.ErrorMessage
import com.library.loans.domain.validateUnexpired
import org.junit.Test
import java.time.LocalDate

class ValidateUnexpiredTest {
    @Test
    fun `it passes through when patronship is active`() {
        val patron = buildPatron(expirationDate = LocalDate.now().plusDays(1))
        assertSuccess(validateUnexpired(patron), patron)
    }

    @Test
    fun `it fails when patronship is expired in the past`() {
        val patron = buildPatron(expirationDate = LocalDate.now().minusDays(7))
        assertFailure(validateUnexpired(patron), ErrorMessage.PatronshipExpired)
    }

    @Test
    fun `it fails when patronship is expired today`() {
        val patron = buildPatron(expirationDate = LocalDate.now())
        assertFailure(validateUnexpired(patron), ErrorMessage.PatronshipExpired)
    }

}