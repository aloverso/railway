

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/library/api/loans/EXPIRED_PATRON'
    }
    response {
        status 400
        body([
            message: "Patronship expired",
        ])
    }
}