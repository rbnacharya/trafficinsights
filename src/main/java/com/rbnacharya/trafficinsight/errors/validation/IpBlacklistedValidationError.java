package com.rbnacharya.trafficinsight.errors.validation;



public class IpBlacklistedValidationError extends ValidationError {
    public IpBlacklistedValidationError() {
        super(ValidationError.Reason.IP_BLACKLISTED);
    }
}
