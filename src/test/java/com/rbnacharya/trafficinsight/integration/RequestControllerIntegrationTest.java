package com.rbnacharya.trafficinsight.integration;

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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IPBlacklistRepository ipBlacklistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UABlacklistRepository uaBlacklistRepository;

    @Autowired
    private HourlyStatsRepository hourlyStatsRepository;





    @Test
    public void whenInvalidRequest_thenReturnsBadRequest() throws Exception{
        mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "  \"customerID\": 1,\n" +
                "  \"timestamp\": 1631535600000\n" +
                "}")).andExpect(status().isBadRequest());
    }



    @Test
    public void whenValidRequest_thenReturnsOk() throws Exception {
        customerRepository.save(new Customer(1L, "Test Customer", true));
        mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(JsonBuilder.withDefaultTime(1L).setRemoteIP("0.0.0.0").build())).andExpect(status().isOk());
    }

    @Test
    public void whenInactiveCustomer_thenReturnsBadRequest() throws Exception {
        customerRepository.save(new Customer(1L, "Test Customer", false));
        mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(JsonBuilder.withDefaultTime(2L).setRemoteIP("0.0.0.0").build())).andExpect(status().isBadRequest());
    }


    @Test
    public void whenBlacklistedIP_thenReturnsBadRequest() throws Exception {
        customerRepository.save(new Customer(1L, "Test Customer", true));
        ipBlacklistRepository.save(new IPBlacklist("192.168.1.1"));
        mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(JsonBuilder.withDefaultTime(1L).setRemoteIP("192.168.1.1").build())).andExpect(status().isBadRequest());
    }

    @Test
    public void whenBlacklistedUA_thenReturnsBadRequest() throws Exception {
        uaBlacklistRepository.save(new UABlacklist("Mozilla/5.0"));
        mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(JsonBuilder.withDefaultTime(1L).setUserAgent("Mozilla/5.0").build())).andExpect(status().isBadRequest());
    }

    @Test
    public void whenNotBlacklisted_thenReturnsOk() throws Exception {
        customerRepository.save(new Customer(1L, "Test Customer", true));
        ipBlacklistRepository.save(new IPBlacklist("192.168.1.1"));
        uaBlacklistRepository.save(new UABlacklist("Mozilla/5.0"));
        mockMvc.perform(post("/request").contentType(MediaType.APPLICATION_JSON).content(JsonBuilder.withDefaultTime(1L).setRemoteIP("192.168.1.2").setUserAgent("Mozilla/5.1").build())).andExpect(status().isOk());
    }

}
