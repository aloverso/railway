package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/library/api/loans/LOOKUP_FAILED'
    }
    response {
        status 500
        body([
            message: "Failed to retrieve patron",
        ])
    }
}