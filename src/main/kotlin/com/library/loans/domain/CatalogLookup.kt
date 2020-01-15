package com.library.loans.domain

interface CatalogLookup {
    fun fetch(isbn: String): Item
}

