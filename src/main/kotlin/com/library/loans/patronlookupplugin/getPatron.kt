package com.library.loans.patronlookupplugin

import com.library.loans.domain.ErrorMessage
import com.library.loans.domain.Patron
import com.library.railway.Failure
import com.library.railway.Success
import com.library.railway.TwoTrack
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

fun getPatronFactory(
        patronLookupUrl: String,
        restTemplate: RestTemplate
): (String) -> TwoTrack<Patron> {
    return { patronId: String ->
        val requestUrl = patronLookupUrl.replace("<PATRON_ID>", patronId)

        val headers = LinkedMultiValueMap<Any, Any>()
        headers.add("Content-Type", "application/json")

        try {
            val patronResponse = restTemplate
                    .exchange(requestUrl, HttpMethod.GET, HttpEntity<Any>(headers), Patron::class.java)
            Success(patronResponse.body!!)
        } catch (e: Exception) {
            Failure(ErrorMessage.GetPatronFailed)
        }
    }
}

