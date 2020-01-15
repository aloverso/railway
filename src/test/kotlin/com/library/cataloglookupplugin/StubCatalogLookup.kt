package com.library.cataloglookupplugin

import com.library.loans.domain.CatalogLookup
import com.library.loans.domain.Item

class StubCatalogLookup : CatalogLookup {
    var stubbedItem: Item? = null

    override fun fetch(isbn: String): Item {
        return stubbedItem!!
    }
}