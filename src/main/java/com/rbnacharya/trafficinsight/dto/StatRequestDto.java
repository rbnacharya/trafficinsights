package com.rbnacharya.trafficinsight.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

public record StatRequestDto(@NotNull(message = "Customer ID is required") Long customerID,
                             @NotNull(message = "Date is required") @PastOrPresent(message = "Date should be in the past or present") LocalDate date) {
    @Override
    public Long customerID() {
        return customerID;
    }

    @Override
    public LocalDate date() {
        return date;
    }

    public Timestamp getStartTime() {
        return new Timestamp(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public Timestamp getEndTime() {
        return new Timestamp(date.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1);
    }

    public StatRequestDto(Long customerID, LocalDate date) {
        this.customerID = customerID;
        this.date = date;
    }
}
