package com.library.loans.domain

data class Item(
        val isbn: String,
        val title: String,
        val author: String,
        val numberOfCopies: Int,
        val copiesOnLoan: Int,
        val publicationYear: Int,
        val publisher: String
)