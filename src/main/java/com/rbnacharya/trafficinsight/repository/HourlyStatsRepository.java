package com.rbnacharya.trafficinsight.repository;

import com.rbnacharya.trafficinsight.model.Customer;
import com.rbnacharya.trafficinsight.model.HourlyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface HourlyStatsRepository extends JpaRepository<HourlyStats, Long> {
    HourlyStats findByCustomerAndTime(Customer customer, Timestamp time);

    Optional<HourlyStats> findByCustomerIdAndTime(Long customerId, Timestamp time);

    List<HourlyStats> findByCustomerIdAndTimeBetween(Long customerId, Timestamp startTime, Timestamp endTime);

    // Group by customer_id and sum request_count and invalid_count after filtering by time for the given customer
    @Query("SELECT SUM(requestCount), SUM(invalidCount) FROM HourlyStats WHERE customer.id = ?1 AND time BETWEEN ?2 AND ?3")
    List<Object[]> findTotalRequestCountAndInvalidCountByCustomerIdAndTimeBetween(Long customerId, Timestamp startTime, Timestamp endTime);

}

