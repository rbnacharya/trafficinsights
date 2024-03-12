package com.rbnacharya.trafficinsight.repository;

import com.rbnacharya.trafficinsight.model.UABlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UABlacklistRepository extends JpaRepository<UABlacklist, Long> {
    UABlacklist findByUserAgent(String userAgent);

    boolean existsByUserAgent(String userAgent);
}
