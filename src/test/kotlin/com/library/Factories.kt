package com.library

import com.library.loans.domain.Item
import com.library.loans.domain.Loan
import com.library.loans.domain.Patron
import java.time.LocalDate
import kotlin.math.truncate


fun random(): Int {
    return truncate(Math.random() * 10000).toInt()
}

fun buildPatron(
        id: String = "id-${random()}",
        firstName: String = "FirstName ${random()}",
        lastName: String = "LastName ${random()}",
        address: String = "Address ${random()}",
        phone: String = "Phone ${random()}",
        patronSince: LocalDate = LocalDate.now(),
        expirationDate: LocalDate = LocalDate.now(),
        loans: List<Loan> = listOf(buildLoan())
): Patron {
    return Patron(
            id = id,
            firstName = firstName,
            lastName = lastName,
            address = address,
            phone = phone,
            patronSince = patronSince,
            expirationDate = expirationDate,
            loans = loans
    )
}

fun buildLoan(
        id: String = "some-id-${random()}",
        checkoutDate: LocalDate = LocalDate.now(),
        items: List<String> = listOf("some-isbn-${random()}")
): Loan {
    return Loan(
            id = id,
            checkoutDate = checkoutDate,
            items = items
    )
}

fun buildItem(
        isbn: String = "some-isbn-${random()}",
        title: String = "some title ${random()}",
        author: String = "some author ${random()}",
        copiesOnLoan: Int = random(),
        numberOfCopies: Int = random(),
        publicationYear: Int = random(),
        publisher: String = "some publisher ${random()}"
): Item {
    return Item(
            isbn = isbn,
            title = title,
            author = author,
            copiesOnLoan = copiesOnLoan,
            numberOfCopies = numberOfCopies,
            publicationYear = publicationYear,
            publisher = publisher
    )
}