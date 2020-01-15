package com.library.loans.domain

import com.library.railway.Success
import com.library.railway.TwoTrack
import java.time.LocalDate

fun extractLoansFactory(catalogLookup: CatalogLookup): (Patron) -> TwoTrack<List<LoanItem>> {
    return { patron: Patron ->
        Success(patron.loans.flatMap { loan ->
            listOf(loan.items.map { isbn ->
                val item = lookupItem(isbn, catalogLookup)

                LoanItem(
                        title = item.title,
                        author = item.author,
                        isbn = isbn,
                        eligibleForRenewal = isEligibleForRenewal(item),
                        dueDate = getDueDate(loan.checkoutDate)
                )
            }.asIterable())
        }.flatten())
    }
}

fun isEligibleForRenewal(item: Item): Boolean {
    return item.numberOfCopies - item.copiesOnLoan > 0
}

fun getDueDate(checkoutDate: LocalDate): LocalDate {
    return checkoutDate.plusDays(21)
}

fun lookupItem(isbn: String, catalogLookup: CatalogLookup): Item {
    return try {
        catalogLookup.fetch(isbn)
    } catch (e: Exception) {
        Item(
                title = "Unable to retrieve title at this time",
                author = "Unable to retrieve author at this time",
                isbn = isbn,
                numberOfCopies = 0,
                copiesOnLoan = 0,
                publicationYear = 0,
                publisher = ""
        )
    }
}