package com.rbnacharya.trafficinsight.repository;

import com.rbnacharya.trafficinsight.model.IPBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPBlacklistRepository extends JpaRepository<IPBlacklist, Long> {
    IPBlacklist findByIp(String ip);

    boolean existsByIp(String ip);
}
