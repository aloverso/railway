package com.library.patronlookupplugin

import com.library.assertFailure
import com.library.getSuccess
import com.library.loans.domain.ErrorMessage
import com.library.loans.domain.Patron
import com.library.loans.patronlookupplugin.getPatronFactory
import com.library.railway.TwoTrack
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withServerError
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate


class GetPatronTest {

    lateinit var restTemplate: RestTemplate
    lateinit var mockServer: MockRestServiceServer
    val lookupUrl = "http://patron-lookup.com/<PATRON_ID>"
    lateinit var getPatron: (String) -> TwoTrack<Patron>

    @Before
    fun setUp() {
        restTemplate = RestTemplate()
        mockServer = MockRestServiceServer.createServer(restTemplate)
        getPatron = getPatronFactory(lookupUrl, restTemplate)
    }

    @Test
    fun `it looks up a patron from the patron API using their ID`() {
        val stubPatron = "{\n  \"id\": \"123456789\",\n  \"firstName\": \"Syl\",\n  \"lastName\": \"Penguin\",\n  \"address\": \"1 South Pole, Antarctica\",\n  \"phone\": \"000-000-0000\",\n  \"patronSince\": \"2018-11-07\",\n  \"expirationDate\": \"2028-11-07\",\n  \"loans\": [\n    {\n      \"id\": \"12345\",\n      \"checkoutDate\": \"2020-01-01\",\n      \"items\": [\"isbn1\", \"isbn2\"]\n    },\n    {\n      \"id\": \"56789\",\n      \"checkoutDate\": \"2020-01-02\",\n      \"items\": [\"isbn3\"]\n    }\n  ]\n}\n"

        mockServer.expect(requestTo("http://patron-lookup.com/123456789"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(stubPatron, MediaType.APPLICATION_JSON))

        val patron = getSuccess(getPatron("123456789"))
        assertThat(patron.firstName).isEqualTo("Syl")
        mockServer.verify()
    }

    @Test
    fun `it fails with GetPatronFailed when lookup returns error code`() {
        mockServer.expect(requestTo("http://patron-lookup.com/123456789"))
                .andRespond(withServerError())

        assertFailure(getPatron("123456789"), ErrorMessage.GetPatronFailed)
        mockServer.verify()
    }

}