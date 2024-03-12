package com.rbnacharya.trafficinsight.service;

import com.rbnacharya.trafficinsight.dto.TrackRequestDto;
import com.rbnacharya.trafficinsight.errors.validation.CustomerNotFoundValidationError;
import com.rbnacharya.trafficinsight.errors.validation.IpBlacklistedValidationError;
import com.rbnacharya.trafficinsight.errors.validation.UABlacklistedValidationError;
import com.rbnacharya.trafficinsight.errors.validation.ValidationError;
import com.rbnacharya.trafficinsight.repository.CustomerRepository;
import com.rbnacharya.trafficinsight.repository.IPBlacklistRepository;
import com.rbnacharya.trafficinsight.repository.UABlacklistRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class RequestValidatorServiceTest {
    @Autowired
    private TrackRequestValidatorService requestValidatorService;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private IPBlacklistRepository ipBlacklistRepository;

    @MockBean
    private UABlacklistRepository uaBlacklistRepository;



    @Test
    void testBlacklistIpNotInList() {
        String customerIp = "0.0.0.0";
        Mockito.when(ipBlacklistRepository.existsByIp(customerIp)).thenReturn(false);
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(true);
        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000L);
        requestDto.setRemoteIP(customerIp);

        assertDoesNotThrow(() -> requestValidatorService.validateRequest(requestDto));

    }

    @Test
    void testBlacklistIpInList() {
        String dummyIp = "1.1.1.1";
        Mockito.when(ipBlacklistRepository.existsByIp(dummyIp)).thenReturn(true);
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(true);

        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000L);
        requestDto.setRemoteIP(dummyIp);

        assertThrows(IpBlacklistedValidationError.class, () -> requestValidatorService.validateRequest(requestDto));
    }

    @Test
    void testBlacklistUaNotInList() {
        String dummyUa = "dummy-ua";
        Mockito.when(uaBlacklistRepository.existsByUserAgent(dummyUa)).thenReturn(false);
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(true);

        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000L);
        requestDto.setUserAgent(dummyUa);

        assertDoesNotThrow(() -> requestValidatorService.validateRequest(requestDto));
    }

    @Test
    void testBlacklistUaInList() {
        String dummyUa = "dummy-ua";
        Mockito.when(uaBlacklistRepository.existsByUserAgent(dummyUa)).thenReturn(true);
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(true);

        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setUserAgent(dummyUa);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000L);

        assertThrows(UABlacklistedValidationError.class, () -> requestValidatorService.validateRequest(requestDto));

    }

    @Test
    void invalidCustomer() {
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(false);

        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000);
        assertThrows(CustomerNotFoundValidationError.class, () -> requestValidatorService.validateRequest(requestDto));
    }

    @Test
    void disabledCustomer() {
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(false);
        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000);
        assertThrows(CustomerNotFoundValidationError.class, () -> requestValidatorService.validateRequest(requestDto));
    }

    @Test
    void timestampInFuture() {
        Mockito.when(customerRepository.existsByIdAndActive(1L, true)).thenReturn(false);
        TrackRequestDto requestDto = new TrackRequestDto();
        requestDto.setCustomerID(1L);
        requestDto.setTimestamp(System.currentTimeMillis() / 1000 + 1000);
        assertThrows(ValidationError.class, () -> requestValidatorService.validateRequest(requestDto));
    }
}