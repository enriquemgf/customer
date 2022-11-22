package com.bradesco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import com.bradesco.customer.Address;
import com.bradesco.customer.Customer;
import com.bradesco.dao.CustomerRepository;

class CustomerServiceTest {

    @Mock
    private CustomerRepository mockRepository;

    @Mock
    private AddressService mockAddressService;

    @InjectMocks
    private CustomerService service;

    @BeforeEach
    void init_mocks() {
        openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        when(this.mockRepository.findAll())
            .thenReturn(Collections.singletonList(mock(Customer.class)));

        assertEquals(1, this.service.getAllCustomers().size());
    }

    @Test
    void testGetAllCustomersByZipCode() {
        when(this.mockRepository.findByAddress_ZipCode(anyString()))
            .thenReturn(Optional.of(Collections.singletonList(mock(Customer.class))));

        assertEquals(1, this.service.getAllCustomersByZipCode("12345-123").size());
    }

    @Test
    void testGetAllCustomersByZipCodeThrowsExceptionWhenNotFound() {
        when(this.mockRepository.findByAddress_ZipCode(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> this.service.getAllCustomersByZipCode("12345-123"));
    }

    @Test
    void testGetCustomerByDocumentId() {
        when(this.mockRepository.findById(anyString()))
            .thenReturn(Optional.of(mock(Customer.class)));

        assertNotNull(this.service.getCustomer("123123123-12"));
    }

    @Test
    void testGetCustomerByDocumentIdThrowsExceptionWhenNotFound() {
        when(this.mockRepository.findById(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> this.service.getCustomer("123123123-12"));
    }

    @Test
    void testCreateCustomer() {
        Customer customer = Customer
            .builder()
            .id("123123123-12")
            .name("Batman")
            .age("20")
            .address(Collections.singleton(new Address()))
            .build();

        when(this.mockRepository.findById(anyString()))
            .thenReturn(Optional.empty());
        when(this.mockAddressService.findAddress(any(Address.class)))
            .thenAnswer(i -> i.getArguments()[0]);
        when(this.mockRepository.save(any(Customer.class)))
            .thenAnswer(i -> i.getArguments()[0]);

        Customer returnedCustomer = this.service.createCustomer(customer);

        verify(this.mockRepository, times(1)).save(any(Customer.class));
        assertEquals(customer, returnedCustomer);
    }

    @Test
    void testCreateCustomerThrowsExceptionWhenIdAlreadyExistsInRepository() {
        Customer customer = Customer
            .builder()
            .id("123123123-12")
            .build();

        when(this.mockRepository.findById(customer.getId()))
            .thenReturn(Optional.of(mock(Customer.class)));

        assertThrows(ResponseStatusException.class, () -> this.service.createCustomer(customer));
    }

    @Test
    void testUpdateCustomerWithoutChangingAnythingShouldNotUpdateLastUpdateInfo() {
        Customer customer = Customer
            .builder()
            .id("123123123-12")
            .name("Batman")
            .age("20")
            .lastUpdateInfo(yesterday())
            .address(Collections.singleton(new Address()))
            .build();

        when(this.mockRepository.findById(customer.getId()))
            .thenReturn(Optional.of(customer));

        Customer returnedCustomer = this.service.updateCustomer(customer, customer.getId());

        assertNotEquals(new Date(), returnedCustomer.getLastUpdateInfo());
    }

    @Test
    void testUpdateCustomerShouldUpdateLastUpdateInfo() {
        Customer customer = Customer
            .builder()
            .id("123123123-12")
            .name("Batman")
            .age("20")
            .lastUpdateInfo(yesterday())
            .address(Collections.singleton(new Address()))
            .build();
        Customer mockCustomer = mock(Customer.class);

        when(mockCustomer.getAddress())
            .thenReturn(Collections.singleton(mock(Address.class)));
        when(this.mockRepository.findById(customer.getId()))
            .thenReturn(Optional.of(mockCustomer));
        when(this.mockRepository.save(any(Customer.class)))
            .thenAnswer(i -> i.getArguments()[0]);

        this.service.updateCustomer(customer, customer.getId());
        verify(this.mockRepository, times(1)).save(any(Customer.class));
        verify(mockCustomer).setLastUpdateInfo(any());
    }

    @Test
    void testUpdateCustomerThrowsExceptionWhenNotFound() {
        Customer customer = Customer
            .builder()
            .id("123123123-12")
            .build();

        when(this.mockRepository.findById(anyString()))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> this.service.updateCustomer(customer, customer.getId()));
    }

    @Test
    void testDeleteCustomer() {

        Customer customer1 = Customer
            .builder()
            .id("123123123-12")
            .name("Batman")
            .age("20")
            .build();
        Customer customer2 = Customer
            .builder()
            .id("123123123-12")
            .build();
        Set<Customer> customerSet = new HashSet<>();
        customerSet.add(customer1);
        customerSet.add(customer2);
        Address address = Address
            .builder()
            .zipCode("12345-678")
            .number("123")
            .customers(customerSet)
            .build();
        customer1.setAddress(Collections.singleton(address));
        customer2.setAddress(Collections.singleton(address));

        when(this.mockRepository.findById(anyString()))
            .thenReturn(Optional.of(customer1));

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        this.service.deleteCustomer(customer1.getId());
        verify(this.mockRepository, times(1)).delete(customerArgumentCaptor.capture());
        assertEquals(0, customerArgumentCaptor.getValue().getAddress().size());
    }

    @Test
    void testDeleteCustomerThrowsExceptionWhenNotFound() {
        Customer customer = Customer
            .builder()
            .id("123123123-12")
            .build();
        when(this.mockRepository.findById(customer.getId()))
            .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> this.service.deleteCustomer(customer.getId()));
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}