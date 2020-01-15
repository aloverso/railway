//package com.example.standard
//
//import com.google.gson.JsonObject
//import com.google.gson.JsonParser
//import org.springframework.web.client.RestTemplate
//import java.time.LocalDateTime
//import java.time.ZoneId
//import java.time.ZoneOffset
//import java.time.format.DateTimeFormatter
//
//enum class ErrorReason {
//    PATRONSHIP_EXPIRED,
//    SYSTEM_ERROR
//}
//
//enum class AvailabilityStatus
//
//class GetLoansException(reason: ErrorReason): Exception()
//
//data class Loan(
//        val dueDate: LocalDateTime,
//        val items: List<LoanItem>
//)
//
//data class LoanItem(
//        val isbn: String,
//        val title: String,
//        val author: String,
//        val eligibleForRenewal: Boolean
//)
//
//interface CatalogLookup {
//    fun fetch(isbn: String): ItemDetails
//}
//
//interface HoldService {
//    fun fetch(): List<String>
//}
//
//data class ItemDetails(
//        val isbn: String,
//        val title: String,
//        val author: String,
//        val availability: AvailabilityStatus,
//        val numberOfCopies: Int,
//        val publicationYear: Int,
//        val publisher: String
//)
//
//@Suppress("TooGenericExceptionCaught")
//class Library(
//        val restTemplate: RestTemplate,
//        val patronLookupUrl: String,
//        val catalogLookup: CatalogLookup,
//        val holdService: HoldService
//) {
//
//    companion object {
//        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter
//                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'UTC'")
//                .withZone(ZoneId.of("UTC"))
//    }
//
//    /*{
//        firstName: "",
//        lastName: "",
//        address: {},
//        phone: "",
//        patronSince: "",
//        expirationDate: ""
//        loans: [
//            {
//                id: "",
//                checkoutDate: "",
//                items: ["isbn1", "isbn2"]
//            },
//            ...
//        ]
//    }*/
//
//    fun getCurrentLoans(patronId: String): List<Loan> {
//        try {
//            val patronResponse = getPatron(patronId)
//
//            if (patronshipExpired(patronResponse)) {
//                throw GetLoansException(ErrorReason.PATRONSHIP_EXPIRED)
//            }
//
//            return patronResponse.getAsJsonArray("loans")
//                    .map { it.asJsonObject }
//                    .map {
//                        val items = it.getAsJsonArray("items")
//                                .map { it.asJsonObject}
//                                .map {
//                                    val isbn = it.get("isbn").asString
//                                    val itemData = catalogLookup.fetch(isbn)
//
//                                    LoanItem(
//                                            isbn = isbn,
//                                            title = itemData.title,
//                                            author = itemData.author,
//                                            eligibleForRenewal = getEligibilityStatus(isbn)
//                                    )
//                                }
//
//
//
//                        Loan(
//                                dueDate = getDueDate(it.get("checkoutDate").asString),
//                                items = items
//                        )
//                    }
//
//        } catch (e: Exception) {
//            throw GetLoansException(ErrorReason.SYSTEM_ERROR)
//        }
//    }
//
//    private fun getEligibilityStatus(isbn: String): Boolean {
//        val itemsOnHold = holdService.fetch()
//        return itemsOnHold.contains(isbn)
//    }
//
//    private fun getDueDate(checkoutDate: String): LocalDateTime {
//        return LocalDateTime
//                .parse(checkoutDate, dateTimeFormatter)
//                .plusDays(21)
//    }
//
//    private fun getPatron(patronId: String): JsonObject {
//        val requestUrl = patronLookupUrl.replace("<PATRON_ID>",patronId)
//        val patronResponse = restTemplate.getForEntity(requestUrl, String::class.java)
//
//        if (!patronResponse.statusCode.is2xxSuccessful) {
//            throw GetLoansException(ErrorReason.SYSTEM_ERROR)
//        }
//
//        return JsonParser().parse(patronResponse.body
//                ?: throw GetLoansException(ErrorReason.SYSTEM_ERROR)).asJsonObject
//    }
//
//    private fun patronshipExpired(patronResponse: JsonObject): Boolean {
//        val expirationDate = LocalDateTime.parse(patronResponse.get("expirationDate").asString, dateTimeFormatter)
//        val utcNow = LocalDateTime.now(ZoneOffset.UTC)
//
//        return utcNow > expirationDate
//    }
//
//}