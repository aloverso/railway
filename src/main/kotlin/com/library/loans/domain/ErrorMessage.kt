package com.library.loans.domain

import org.springframework.http.HttpStatus


enum class ErrorMessage {
    // loopkup errors
    GetPatronFailed,
    ItemLookupFailed,
    ItemLookupCatalogFailed,

    // validation errors
    PatronshipExpired,
//    PatronNeedsToUpdateContactInfo,

    // default error
    SystemError
}

fun mapErrorMessage(errorMessage: ErrorMessage): ErrorResponse {
        return when(errorMessage) {
            ErrorMessage.PatronshipExpired -> ErrorResponse(
                    status = HttpStatus.BAD_REQUEST,
                    message = "Patronship expired"
            )
            ErrorMessage.SystemError -> ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = "Something went wrong"
            )
            ErrorMessage.GetPatronFailed -> ErrorResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = "Failed to retrieve patron"
            )
        }
}

data class ErrorResponse(
        var status: HttpStatus,
        var message: String
)