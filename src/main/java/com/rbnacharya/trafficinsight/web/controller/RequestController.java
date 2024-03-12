package com.rbnacharya.trafficinsight.web.controller;

import com.rbnacharya.trafficinsight.dto.TrackRequestDto;
import com.rbnacharya.trafficinsight.dto.TrackResponseDto;
import com.rbnacharya.trafficinsight.errors.validation.CustomerNotFoundValidationError;
import com.rbnacharya.trafficinsight.errors.validation.ValidationError;
import com.rbnacharya.trafficinsight.service.StatsService;
import com.rbnacharya.trafficinsight.service.TrackRequestValidatorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {

    @Autowired
    private TrackRequestValidatorService requestValidatorService;

    @Autowired
    private StatsService statsService;

    @PostMapping("/request")
    public ResponseEntity<?> handleRequest(@Valid  @RequestBody TrackRequestDto requestDto, HttpServletRequest httpRequest){

        try {
            requestValidatorService.validateRequest(requestDto);
            statsService.updateRequestCount(requestDto.getCustomerID(), requestDto.getTimestampAsDate(), true);
            requestValidatorService.handleValidRequest(requestDto);
            return  ResponseEntity.ok(new TrackResponseDto("Success", "Updated request count"));
        } catch (ValidationError e) {
            if(!(e instanceof CustomerNotFoundValidationError)){
                statsService.updateRequestCount(requestDto.getCustomerID(), requestDto.getTimestampAsDate(), false);
            }
            return new ResponseEntity<>( e.getBody(), e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
