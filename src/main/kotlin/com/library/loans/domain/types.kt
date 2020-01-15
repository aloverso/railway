package com.library.loans.domain

import com.library.railway.TwoTrack

typealias GetCurrentLoans = (String) -> TwoTrack<List<LoanItem>>

typealias GetPatron = (TwoTrack<String>) -> TwoTrack<Patron>
typealias ValidatePatron = (TwoTrack<Patron>) -> TwoTrack<Patron>
typealias ValidatePatronAddress = (TwoTrack<Patron>) -> TwoTrack<Patron>
typealias ExtractLoans = (TwoTrack<Patron>) -> TwoTrack<List<LoanItem>>

