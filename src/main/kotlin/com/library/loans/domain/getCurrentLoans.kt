package com.library.loans.domain

import com.library.railway.input
import com.library.railway.pipe

fun getCurrentLoansFactory(
        getPatron: GetPatron,
        validatePatron: ValidatePatron,
        extractLoans: ExtractLoans
): GetCurrentLoans {
    return { patronId: String ->
        input(patronId)
                .pipe(getPatron)
                .pipe(validatePatron)
                .pipe(extractLoans)
    }
}