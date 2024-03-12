package com.rbnacharya.trafficinsight.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;
import java.sql.Timestamp;

public class TrackRequestDto {

    @NotNull(message = "Customer ID is required")
    @Min(value = 1, message = "Customer ID must be greater than 0")
    private Long customerID;

    @NotNull(message = "Tag ID is required")
    @Min(value = 1, message = "Tag ID must be greater than 0")
    private Long tagID;

    @NotBlank(message = "User ID cannot be empty")
    private String userID;

    @NotNull(message = "Timestamp is required")
    @Min(value = 1, message = "Timestamp must be a positive number")
    private Long timestamp;

    @NotBlank(message = "Remote IP cannot be empty")
    private String remoteIP;

    @NotBlank(message = "User agent cannot be empty")
    private String userAgent;


    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

    public void setTagID(Long tagID) {
        this.tagID = tagID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public Long getTagID() {
        return tagID;
    }

    public String getUserID() {
        return userID;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getRemoteIP() {
        return remoteIP;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public Timestamp getTimestampAsDate() {
        return new Timestamp(timestamp * 1000);
    }
}
