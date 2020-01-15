package com.library.loans.config

import com.library.loans.catalogplugin.RandomCatalogLookup
import com.library.loans.domain.CatalogLookup
import com.library.loans.domain.ExtractLoans
import com.library.loans.domain.GetCurrentLoans
import com.library.loans.domain.GetPatron
import com.library.loans.domain.ValidatePatron
import com.library.loans.domain.extractLoansFactory
import com.library.loans.domain.getCurrentLoansFactory
import com.library.loans.domain.validateUnexpired
import com.library.loans.patronlookupplugin.getPatronFactory
import com.library.railway.convertToTwoTrack
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class LoansConfig {

    @Bean
    fun getCurrentLoans(
            getPatron: GetPatron,
            validatePatron: ValidatePatron,
            extractLoans: ExtractLoans
    ): GetCurrentLoans {
        return getCurrentLoansFactory(getPatron, validatePatron, extractLoans)
    }

    @Bean
    fun getPatron(
            @Value("\${patron-lookup.url}") lookupUrl: String,
            restTemplate: RestTemplate
    ): GetPatron {
        return convertToTwoTrack(getPatronFactory(lookupUrl, restTemplate))
    }

    @Bean
    fun validatePatron(): ValidatePatron {
        return convertToTwoTrack(::validateUnexpired)
    }

    @Bean
    fun extractLoans(catalogLookup: CatalogLookup): ExtractLoans {
        return convertToTwoTrack(extractLoansFactory(catalogLookup))
    }

    @Bean
    fun catalogLookup(): CatalogLookup {
        return RandomCatalogLookup()
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}