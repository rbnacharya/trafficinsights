package com.rbnacharya.trafficinsight.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbnacharya.trafficinsight.dto.StatResponseDto;
import com.rbnacharya.trafficinsight.model.Customer;
import com.rbnacharya.trafficinsight.model.IPBlacklist;
import com.rbnacharya.trafficinsight.model.UABlacklist;
import com.rbnacharya.trafficinsight.repository.CustomerRepository;
import com.rbnacharya.trafficinsight.repository.HourlyStatsRepository;
import com.rbnacharya.trafficinsight.repository.IPBlacklistRepository;
import com.rbnacharya.trafficinsight.repository.UABlacklistRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private IPBlacklistRepository ipBlacklistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UABlacklistRepository uaBlacklistRepository;

    @Autowired
    private HourlyStatsRepository hourlyStatsRepository;

    Long CUSTOMER_ID = 1L;
    String BAD_IP_ADDRESS = "192.168.1.1";
    String BAD_USER_AGENT = "Bad User Agent";

    String GOOD_IP_ADDRESS = "192.168.2.2";
    String GOOD_USER_AGENT = "Good User Agent";



    // Utility method to add a request to the database
    public ResultActions executeRequest(Long customerId, Long timestamp, String remoteIP, String userAgent) throws Exception{
        return mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(
                JsonBuilder.withDefaultTime(customerId).setTimestamp(timestamp).setRemoteIP(remoteIP).setUserAgent(userAgent).build())
        );
    }

    public static List<Long> generateTimestampsForTesting(LocalDateTime yesterday, String[] times) {
        // List to hold your timestamps
        List<Long> timestamps = new ArrayList<>();


        for (String time : times) {
            // Split the time string into hours and minutes
            int hour = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);

            // Set the specific time for yesterday
            LocalDateTime specificTimeYesterday = yesterday.withHour(hour).withMinute(minute).withSecond(0).withNano(0);

            // Convert LocalDateTime to ZonedDateTime to get the instant
            ZonedDateTime zdt = specificTimeYesterday.atZone(ZoneId.systemDefault());

            // Convert to timestamp in seconds
            long timestamp = zdt.toEpochSecond();

            // Add to list
            timestamps.add(timestamp);
        }

        return timestamps;
    }


    // Test cases will be added here

    @Test
    public void testCustomerStatistics() throws Exception {
        customerRepository.save(new Customer(CUSTOMER_ID, "Test Customer", true));
        ipBlacklistRepository.save(new IPBlacklist(BAD_IP_ADDRESS));
        uaBlacklistRepository.save(new UABlacklist(BAD_USER_AGENT));


        String[] times = {"10:05", "10:10", "11:30", "12:15"};
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        String yesterdayString = yesterday.toLocalDate().toString();
        List<Long> timestamps = generateTimestampsForTesting(LocalDateTime.now().minusDays(1), times);

        executeRequest(CUSTOMER_ID, timestamps.get(0), GOOD_IP_ADDRESS, GOOD_USER_AGENT).andExpect(status().isOk()); // Valid request
        executeRequest(CUSTOMER_ID, timestamps.get(1), BAD_IP_ADDRESS, GOOD_USER_AGENT).andExpect(status().isBadRequest());; // Invalid Request
        executeRequest(CUSTOMER_ID, timestamps.get(2), GOOD_IP_ADDRESS, BAD_USER_AGENT).andExpect(status().isBadRequest());; // Invalid Request
        executeRequest(CUSTOMER_ID, timestamps.get(3), GOOD_IP_ADDRESS, GOOD_USER_AGENT).andExpect(status().isOk());; // Valid Request

        mockMvc.perform(get("/statistics?customerID=1&date=" + yesterdayString)).andExpect(status().isOk()).andExpect(result -> {
            String content = result.getResponse().getContentAsString();
            StatResponseDto[] stats = objectMapper.readValue(content, StatResponseDto[].class);
            Assertions.assertEquals(1, stats.length);
            Assertions.assertEquals(2, stats[0].invalidCount());
            Assertions.assertEquals(4, stats[0].requestCount());

        });

    }

}
