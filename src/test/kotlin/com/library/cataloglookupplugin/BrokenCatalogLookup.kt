package com.library.cataloglookupplugin

import com.library.loans.domain.CatalogLookup
import com.library.loans.domain.Item

class BrokenCatalogLookup : CatalogLookup {
    override fun fetch(isbn: String): Item {
        throw Exception("SYSTEM FAILURE")
    }
}