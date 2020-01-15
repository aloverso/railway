package com.library.loans.web

import com.library.loans.domain.GetCurrentLoans
import com.library.loans.domain.LoanItem
import com.library.loans.domain.mapErrorMessage
import com.library.railway.Failure
import com.library.railway.Success
import com.library.railway.TwoTrack
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class LoansController(
        val getCurrentLoans: GetCurrentLoans
) {

    @GetMapping("/library/api/loans/{patronId}")
    fun getLoansForPatron(
            @PathVariable(value="patronId") patronId: String
    ): ResponseEntity<Any> {
        return when(val currentLoans: TwoTrack<List<LoanItem>> = getCurrentLoans(patronId)) {
            is Success -> ResponseEntity.ok(currentLoans.b)
            is Failure -> {
                val errorResponse = mapErrorMessage(currentLoans.a)
                ResponseEntity
                        .status(errorResponse.status)
                        .body(errorResponse)
            }
        }
    }

    fun renewItem(patronId: String, isbn: String) {
        TODO("Not implemented")
    }
}