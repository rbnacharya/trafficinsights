package com.rbnacharya.trafficinsight.service;

import com.rbnacharya.trafficinsight.dto.StatRequestDto;
import com.rbnacharya.trafficinsight.dto.StatResponseDto;
import com.rbnacharya.trafficinsight.model.Customer;
import com.rbnacharya.trafficinsight.model.HourlyStats;
import com.rbnacharya.trafficinsight.repository.CustomerRepository;
import com.rbnacharya.trafficinsight.repository.HourlyStatsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class StatsService {
    @Autowired
    private HourlyStatsRepository hourlyStatsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void updateRequestCount(Long customerId, Timestamp timestamp, boolean isValid){
        Timestamp currentHour = getCurrentHour(timestamp);

        HourlyStats hourlyStats = hourlyStatsRepository.findByCustomerIdAndTime(customerId, currentHour).orElse(null);
        if(hourlyStats == null){
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
            hourlyStats = new HourlyStats(customer, currentHour, 0L, 0L);
        }
        hourlyStats.incrementRequestCount();
        if(!isValid){
            hourlyStats.incrementInvalidCount();
        }
        hourlyStatsRepository.save(hourlyStats);
    }

    public Timestamp getCurrentHour(Timestamp timestamp){
        // Rounding down to the nearest hour
        ZonedDateTime rounded = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault()).withMinute(0).withSecond(0).withNano(0);
        return new Timestamp(rounded.toInstant().toEpochMilli());
    }

    public List<StatResponseDto> getStatistics(StatRequestDto requestDto) {
        List<Object[]> stats = hourlyStatsRepository.findTotalRequestCountAndInvalidCountByCustomerIdAndTimeBetween(requestDto.customerID(), requestDto.getStartTime(), requestDto.getEndTime());
        return stats.stream().map(stat -> new StatResponseDto(requestDto.customerID(), requestDto.date(), (Long) stat[0], (Long) stat[1])).toList();
    }
}
