package com.library.domain

import com.library.buildItem
import com.library.buildLoan
import com.library.buildPatron
import com.library.cataloglookupplugin.BrokenCatalogLookup
import com.library.cataloglookupplugin.StubCatalogLookup
import com.library.getSuccess
import com.library.loans.domain.LoanItem
import com.library.loans.domain.Patron
import com.library.loans.domain.extractLoansFactory
import com.library.railway.TwoTrack
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.LocalDate


class ExtractLoansTest {

    lateinit var extractLoans: (Patron) -> TwoTrack<List<LoanItem>>
    lateinit var stubCatalogLookup: StubCatalogLookup

    @Before
    fun setUp() {
        stubCatalogLookup = StubCatalogLookup()
        extractLoans = extractLoansFactory(stubCatalogLookup)
    }

    @Test
    fun `it returns loans for a patron`() {
        stubCatalogLookup.stubbedItem = buildItem()

        val patron = buildPatron(loans = listOf(
                buildLoan(
                        items = listOf("123", "456")
                ),
                buildLoan(items = listOf("789"))
        ))

        val loans = getSuccess(extractLoans(patron))
        assertThat(loans.map { item -> item.isbn }).isEqualTo(listOf("123", "456", "789"))
    }

    @Test
    fun `it populates item with details from catalog`() {
        stubCatalogLookup.stubbedItem = buildItem(
                title = "kotlin is rad",
                author = "anne loverso"
        )

        val patron = buildPatron(loans = listOf(
                buildLoan(items = listOf("123"))
        ))

        extractLoans(patron)

        val loans = getSuccess(extractLoans(patron))
        assertThat(loans[0].title).isEqualTo("kotlin is rad")
        assertThat(loans[0].author).isEqualTo("anne loverso")
        assertThat(loans[0].isbn).isEqualTo("123")
    }

    @Test
    fun `it sets the due date as 21 days past the checkout date`() {
        stubCatalogLookup.stubbedItem = buildItem()

        val patron = buildPatron(loans = listOf(
                buildLoan(items = listOf("123"), checkoutDate = LocalDate.of(2020,1,1))
        ))

        val loans = getSuccess(extractLoans(patron))
        assertThat(loans[0].dueDate).isEqualTo(LocalDate.of(2020,1,22))
    }

    @Test
    fun `it is eligible for renewal if there are copies available not on loan`() {
        stubCatalogLookup.stubbedItem = buildItem(numberOfCopies = 10, copiesOnLoan = 1)

        val patron = buildPatron(loans = listOf(
                buildLoan(items = listOf("123"))
        ))

        val loans = getSuccess(extractLoans(patron))
        assertThat(loans[0].eligibleForRenewal).isEqualTo(true)
    }


    @Test
    fun `it is eligible for renewal if there are not copies available not on loan`() {
        stubCatalogLookup.stubbedItem = buildItem(numberOfCopies = 10, copiesOnLoan = 10)

        val patron = buildPatron(loans = listOf(
                buildLoan(items = listOf("123"))
        ))

        val loans = getSuccess(extractLoans(patron))
        assertThat(loans[0].eligibleForRenewal).isEqualTo(false)
    }

    @Test
    fun `it returns error fields when item cannot be found`() {
        val brokenCatalogLookup = BrokenCatalogLookup()
        extractLoans = extractLoansFactory(brokenCatalogLookup)

        val patron = buildPatron(loans = listOf(
                buildLoan(items = listOf("123"), checkoutDate = LocalDate.of(2020,1,1))
        ))

        val loans = getSuccess(extractLoans(patron))

        assertThat(loans[0].title).isEqualTo("Unable to retrieve title at this time")
        assertThat(loans[0].author).isEqualTo("Unable to retrieve author at this time")
        assertThat(loans[0].isbn).isEqualTo("123")
        assertThat(loans[0].eligibleForRenewal).isEqualTo(false)
        assertThat(loans[0].dueDate).isEqualTo(LocalDate.of(2020,1,22))
    }
}
