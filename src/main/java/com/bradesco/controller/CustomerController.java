package com.bradesco.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bradesco.customer.Customer;
import com.bradesco.dto.CustomerDto;
import com.bradesco.service.CustomerService;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@RestController
@RequestMapping("/v1/customer")
public class CustomerController {
    private static final Logger LOGGER = LogManager.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Autowired
    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers(@RequestParam Optional<String> zipCode){
        LOGGER.info("Get all customers with optional parm of " + zipCode.orElse("null"));

        final List<Customer> customers = zipCode.map(customerService::getAllCustomersByZipCode).orElseGet(customerService::getAllCustomers);
        return new ResponseEntity<>(customers.stream().map(CustomerDto::fromCustomer).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/{documentId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable String documentId) {
        LOGGER.info("Get customer by documentId " + documentId);

        final Customer customer = this.customerService.getCustomer(documentId);
        return new ResponseEntity<>(CustomerDto.fromCustomer(customer), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        LOGGER.info("Create customer with documentId " + customerDto.getDocumentId());

        final Customer customer = this.customerService.createCustomer(Customer.fromCustomerDto(customerDto));
        return new ResponseEntity<>(CustomerDto.fromCustomer(customer), HttpStatus.OK);
    }

    @PutMapping(value = "/{documentId}")
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody @Valid CustomerDto customerDto, @PathVariable String documentId) {
        LOGGER.info("Update customer with documentId " + documentId);

        final Customer customer = this.customerService.updateCustomer(Customer.fromCustomerDto(customerDto), documentId);
        return new ResponseEntity<>(CustomerDto.fromCustomer(customer), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{documentId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String documentId) {
        LOGGER.info("Delete customer with documentId " + documentId);

        this.customerService.deleteCustomer(documentId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
