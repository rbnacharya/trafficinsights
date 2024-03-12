package com.rbnacharya.trafficinsight.service;

import com.rbnacharya.trafficinsight.dto.StatRequestDto;
import com.rbnacharya.trafficinsight.errors.validation.InvalidRequestValidationError;
import org.springframework.stereotype.Service;

@Service
public class StatRequestValidatorService {
    public void validateStatRequest(StatRequestDto statRequestDto) {
        if (statRequestDto.customerID() == null) {
            throw new InvalidRequestValidationError("customerID is required");
        }
        if (statRequestDto.date() == null) {
            throw new IllegalArgumentException("Day is required");
        }
    }
}
