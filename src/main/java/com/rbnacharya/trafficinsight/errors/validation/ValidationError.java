package com.rbnacharya.trafficinsight.errors.validation;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNullApi;
import org.springframework.web.ErrorResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ValidationError extends RuntimeException implements ErrorResponse {

    @Override
    public HttpStatusCode getStatusCode() {
        return BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
        return ProblemDetail.forStatusAndDetail(BAD_REQUEST, "Invalid input: Reason = " + field);
    }

    public enum Reason {
            INVALID_TIMESTAMP,
            INVALID_REQUEST_STRUCTURE,
            IP_BLACKLISTED,
            UA_BLACKLISTED,
            CUSTOMER_NOT_FOUND
        }
        Reason field;
        public ValidationError(Reason field) {
            super("Invalid input: Reason = " + field);
            this.field = field;
        }

        public ValidationError(Reason field, String moreInfo) {
            super("Invalid input: Reason = " + field + " & More Info= " + moreInfo);
            this.field = field;
        }
}
