package com.rbnacharya.trafficinsight.repository;

import com.rbnacharya.trafficinsight.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByIdAndActive(Long id, boolean active);
}
