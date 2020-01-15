package com.library.loans.catalogplugin

import com.library.loans.domain.CatalogLookup
import com.library.loans.domain.Item
import kotlin.math.truncate

fun random(): Int {
    return truncate(Math.random() * 10000).toInt()
}

class RandomCatalogLookup: CatalogLookup {
    override fun fetch(isbn: String): Item {
        return Item(
                isbn = "some-isbn-${random()}",
                title = "some title ${random()}",
                author = "some author ${random()}",
                copiesOnLoan = random(),
                numberOfCopies = random(),
                publicationYear = random(),
                publisher = "some publisher ${random()}"
        )
    }
}