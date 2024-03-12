package com.rbnacharya.trafficinsight.integration;

import java.time.Instant;

public class JsonBuilder {
    public static String buildRequestJson(Long customerId, Long timestamp, String tagID, String userID, String remoteIP, String userAgent){
        return "{\n" +
                "  \"customerID\": " + customerId + ",\n" +
                "  \"timestamp\": " + timestamp + ",\n" +
                "  \"tagID\": \"" + tagID + "\",\n" +
                "  \"userID\": \"" + userID + "\",\n" +
                "  \"remoteIP\": \"" + remoteIP + "\",\n" +
                "  \"userAgent\": \"" + userAgent + "\"\n" +
                "}";
    }

    public static String buildRequestJson(Long customerId, Long timestamp){
        return "{\n" +
                "  \"customerID\": " + customerId + ",\n" +
                "  \"timestamp\": " + timestamp + "\n" +
                "}";
    }

    Long customerID;
    Long timestamp;
    Long tagID;
    String userID;
    String remoteIP;
    String userAgent;

    public JsonBuilder setCustomerID(Long customerID) {
        this.customerID = customerID;
        return this;
    }

    public JsonBuilder setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public JsonBuilder setTagID(Long tagID) {
        this.tagID = tagID;
        return this;
    }

    public JsonBuilder setUserID(String userID) {
        this.userID = userID;
        return this;
    }

    public JsonBuilder setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
        return this;
    }

    public JsonBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String build() {
        return "{\n" +
                "  \"customerID\": " + customerID + ",\n" +
                "  \"timestamp\": " + timestamp + ",\n" +
                "  \"tagID\": \"" + tagID + "\",\n" +
                "  \"userID\": \"" + userID + "\",\n" +
                "  \"remoteIP\": \"" + remoteIP + "\",\n" +
                "  \"userAgent\": \"" + userAgent + "\"\n" +
                "}";
    }

    public static JsonBuilder withDefaultTime(Long customerID) {
        return new JsonBuilder().setCustomerID(customerID).setTimestamp(Instant.now().getEpochSecond()).setUserID("dummy-user").setTagID(1L);
    }

}
