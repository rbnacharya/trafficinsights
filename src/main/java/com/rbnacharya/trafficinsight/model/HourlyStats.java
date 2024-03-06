package com.rbnacharya.trafficinsight.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.sql.Timestamp;

@Entity
@Table(name = "hourly_stats", uniqueConstraints = {@UniqueConstraint(columnNames = {"customer_id", "time"}, name = "unique_customer_time")}, indexes = {@Index(columnList = "customer_id", name = "customer_idx")})
public class HourlyStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "hourly_stats_customer_id"))
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Customer customer;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "request_count", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long requestCount;

    @Column(name = "invalid_count", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long invalidCount;

}
