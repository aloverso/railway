package contracts

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/library/api/loans/12345'
    }
    response {
        status 200
        body([
		    [
                isbn: $(anyNonBlankString()),
                title: $(anyNonBlankString()),
                author: $(anyNonBlankString()),
                eligibleForRenewal: $(aBoolean()),
                dueDate: $(anyDate()),
            ]
		])
    }
}