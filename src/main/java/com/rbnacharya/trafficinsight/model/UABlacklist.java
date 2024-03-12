package com.rbnacharya.trafficinsight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "ua_blacklist")
public class UABlacklist {
    @Id
    private String userAgent;

    public UABlacklist(String userAgent){
        this.userAgent = userAgent;
    }

    public UABlacklist() {
    }
}
