package com.rbnacharya.trafficinsight.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean active;

    public Customer(String name, boolean active){
        this.name = name;
        this.active = active;
    }

    public Customer(Long id, String name, boolean active){
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public boolean isDisabled() {
        return !active;
    }

    public Long getId() {
        return id;
    }

    protected Customer() {
    }
}
