package com.library.loans.domain

import com.library.railway.Failure
import com.library.railway.Success
import com.library.railway.TwoTrack
import java.time.LocalDate

fun extractLoansFactory(catalogLookup: CatalogLookup): (Patron) -> TwoTrack<List<LoanItem>> {
    return { patron: Patron ->
        lookupEachItem(catalogLookup)(patron)
                .pipe()
    }
}

fun lookupEachItem(catalogLookup: CatalogLookup): (Patron) -> List<TwoTrack<LoanItem>> {
    return { patron: Patron ->
        patron.loans.flatMap { loan ->
            listOf(loan.items.map { isbn ->
                try {
                    val item = catalogLookup.fetch(isbn)
                    Success(LoanItem(
                            title = item.title,
                            author = item.author,
                            isbn = isbn,
                            eligibleForRenewal = isEligibleForRenewal(item),
                            dueDate = getDueDate(loan.checkoutDate)
                    ))
                } catch (e: Exception) {
                    Failure(ErrorMessage.ItemLookupFailed)
                }
            }.asIterable())
        }.flatten()
    }
}

//fun condenseItems(twoTrackItems: List<TwoTrack<LoanItem>>): TwoTrack<List<LoanItem>> {
//    twoTrackItems.containsAll()
//}

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