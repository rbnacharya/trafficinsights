package com.rbnacharya.trafficinsight.service;

import com.rbnacharya.trafficinsight.dto.TrackRequestDto;
import com.rbnacharya.trafficinsight.errors.validation.CustomerNotFoundValidationError;
import com.rbnacharya.trafficinsight.errors.validation.IpBlacklistedValidationError;
import com.rbnacharya.trafficinsight.errors.validation.UABlacklistedValidationError;
import com.rbnacharya.trafficinsight.errors.validation.ValidationError;
import com.rbnacharya.trafficinsight.repository.CustomerRepository;
import com.rbnacharya.trafficinsight.repository.IPBlacklistRepository;
import com.rbnacharya.trafficinsight.repository.UABlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TrackRequestValidatorService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private IPBlacklistRepository ipBlacklistRepository;

    @Autowired
    private UABlacklistRepository uaBlacklistRepository;

    public boolean validateRequest(TrackRequestDto requestDto) {
        if(!hasValidCustomer(requestDto)) {
            throw new CustomerNotFoundValidationError();
        }
        if (!checkIfTimestampIsValid(requestDto)){
            throw new ValidationError(ValidationError.Reason.INVALID_TIMESTAMP, "Timestamp should not be more than 1 year old or in future.");
        }
        if (isIPBlacklisted(requestDto)) {
            throw new IpBlacklistedValidationError();
        }
        if (isUABlacklisted(requestDto)) {
            throw new UABlacklistedValidationError();
        }
        return true;
    }

    public boolean hasValidCustomer(TrackRequestDto requestDto) {
        return requestDto.getCustomerID() != null && customerRepository.existsByIdAndActive(requestDto.getCustomerID(), true);
    }

    public boolean checkIfTimestampIsValid(TrackRequestDto requestDto) {
        // check if timestamp is valid, Considering timestamp is valid if it is not less than 1 year from now, and not in future
        long currentTime = System.currentTimeMillis();
        long requestTimeInMillis = requestDto.getTimestamp() * 1000L;
        return requestTimeInMillis  >= (currentTime - 31556952000L) && requestTimeInMillis < currentTime;
    }




    private boolean isIPBlacklisted(TrackRequestDto requestDto) {
        return ipBlacklistRepository.existsByIp(requestDto.getRemoteIP());
    }

    private boolean isUABlacklisted(TrackRequestDto requestDto) {
        return uaBlacklistRepository.existsByUserAgent(requestDto.getUserAgent());
    }

    public void handleValidRequest(TrackRequestDto requestDto) {
        // handle request
    }

}
