package com.rbnacharya.trafficinsight.web.controller;

import com.rbnacharya.trafficinsight.dto.StatRequestDto;
import com.rbnacharya.trafficinsight.dto.StatResponseDto;
import com.rbnacharya.trafficinsight.service.StatRequestValidatorService;
import com.rbnacharya.trafficinsight.service.StatsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class StatisticsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private StatRequestValidatorService requestValidatorService;

    @GetMapping("/statistics")
    public List<StatResponseDto> getStatistics(@RequestParam @NotNull Long customerID,
                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // If the method is called, it means customerID is not null, and date is valid and formatted correctly.
        return statsService.getStatistics(new StatRequestDto(customerID, date));
    }
}
