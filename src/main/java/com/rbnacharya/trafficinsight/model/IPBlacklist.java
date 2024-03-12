package com.rbnacharya.trafficinsight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "ip_blacklist")
public class IPBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ip;

    public String getIp() {
        return ip;
    }

    public Long getId() {
        return id;
    }

    public IPBlacklist(String ip) {
        this.ip = ip;
    }

    public IPBlacklist() {
    }
}
