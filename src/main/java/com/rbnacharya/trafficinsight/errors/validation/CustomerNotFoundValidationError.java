package com.rbnacharya.trafficinsight.errors.validation;


public class CustomerNotFoundValidationError extends ValidationError {
    public CustomerNotFoundValidationError() {
        super(Reason.CUSTOMER_NOT_FOUND);
    }
}
