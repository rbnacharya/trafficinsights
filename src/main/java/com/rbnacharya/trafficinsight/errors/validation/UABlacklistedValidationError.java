package com.rbnacharya.trafficinsight.errors.validation;


public class UABlacklistedValidationError extends ValidationError {
    public UABlacklistedValidationError() {
        super(Reason.UA_BLACKLISTED);
    }
}
