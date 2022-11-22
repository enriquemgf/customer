package com.bradesco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.bradesco.customer.Address;
import com.bradesco.dao.AddressRepository;

class AddressServiceTest {

    @Mock
    private AddressRepository mockRepository;

    @InjectMocks
    private AddressService service;

    @BeforeEach
    void init_mocks() {
        openMocks(this);
    }

    @Test
    void testFindAddressReturnsSameParmAddressWhenNotFoundInRepository() {
        final Address address = Address
            .builder()
            .zipCode("12345-123")
            .number("123")
            .build();
        when(this.mockRepository.findByZipCodeAndNumber(anyString(), anyString())).thenReturn(Optional.empty());
        final Address returnedAddress = this.service.findAddress(address);

        assertEquals(address, returnedAddress);

    }

    @Test
    void testFindAddressReturnsAddressFromRepositoryWhenFound() {
        final Address address = Address
            .builder()
            .zipCode("12345-123")
            .number("123")
            .build();
        when(this.mockRepository.findByZipCodeAndNumber(anyString(), anyString())).thenReturn(Optional.of(mock(Address.class)));
        final Address returnedAddress = this.service.findAddress(address);

        assertNotEquals(address, returnedAddress);
    }
}