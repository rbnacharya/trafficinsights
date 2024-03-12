package com.rbnacharya.trafficinsight.errors.validation;

public class InvalidRequestValidationError extends ValidationError{
    public InvalidRequestValidationError() {
        super(ValidationError.Reason.INVALID_REQUEST_STRUCTURE);
    }

    public InvalidRequestValidationError(String moreInfo) {
        super(ValidationError.Reason.INVALID_REQUEST_STRUCTURE, moreInfo);
    }
}
