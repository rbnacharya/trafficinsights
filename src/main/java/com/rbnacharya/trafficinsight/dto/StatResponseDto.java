package com.rbnacharya.trafficinsight.dto;

import com.rbnacharya.trafficinsight.model.HourlyStats;

import java.time.LocalDate;
import java.util.List;

public record StatResponseDto(Long customerID, LocalDate date, Long requestCount, Long invalidCount) {

    public static StatResponseDto fromStat(HourlyStats stat) {
        return new StatResponseDto(
                stat.getCustomer().getId(),
                stat.getTime().toLocalDateTime().toLocalDate(),
                stat.getRequestCount(),
                stat.getInvalidCount()
        );
    }

    public static List<StatResponseDto> from(List<HourlyStats> stats) {
        return stats.stream().map(StatResponseDto::fromStat).toList();
    }
}
