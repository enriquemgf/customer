package com.bradesco.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bradesco.customer.Customer;
import com.bradesco.dao.CustomerRepository;

/**
 * Customer service to handle insert, update, delete and query customers along with their dependencies.
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final AddressService addressService;

    @Autowired
    public CustomerService(final CustomerRepository customerRepository, final AddressService addressService){
        this.repository = customerRepository;
        this.addressService = addressService;
    }

    /**
     * Get all the customers in the repository.
     */
    public List<Customer> getAllCustomers(){
        return this.repository.findAll();
    }

    /**
     * Gets all the customers in the repository that have the same address zipcode.
     * @param zipCode for filtering
     * @throws ResponseStatusException if no customer with zip parm is found
     */
    public List<Customer> getAllCustomersByZipCode(final String zipCode) throws ResponseStatusException{
        return this.repository.findByAddress_ZipCode(zipCode).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "No customer with zipCode " + zipCode + " found."));
    }

    /**
     * Get a customer in the repository filtering by documentId.
     * @param documentId for filtering
     * @throws ResponseStatusException if no customer with documentId parm is found
     */
    public Customer getCustomer(final String documentId){
        return this.repository.findById(documentId).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + documentId + " not found."));
    }

    /**
     * Creates a customer in the repository.
     * Before saving the customer address, we try to find an address with same zip code and number to reuse as part
     * of the business requirements by calling {@link AddressService#findAddress}.
     * @param customer to be added
     * @throws ResponseStatusException if customer with same documentId parm already exists
     */
    public Customer createCustomer(final Customer customer) throws ResponseStatusException {
        if (this.repository.findById(customer.getId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer with same document Id already exists.");
        }

        customer.setAddress(customer.getAddress()
            .stream()
            .map(addressService::findAddress)
            .collect(Collectors.toSet()));
        return this.repository.save(customer);
    }

    /**
     * Update a customer in the repository. Document Id cannot be changed.
     * Before saving the customer address, we try to find an address with same zip code and number to reuse as part
     * of the business requirements by calling {@link AddressService#findAddress}.
     * @param newCustomer object with new customer definition
     * @param documentId identifier of the customer to be updated
     * @throws ResponseStatusException if no customer with documentId parm is found
     */
    public Customer updateCustomer(final Customer newCustomer, final String documentId) throws ResponseStatusException {
        return this.repository.findById(documentId).map(customer -> {
            if (customer.equals(newCustomer)){
                return customer;
            }

            customer.setName(newCustomer.getName());
            customer.setAge(newCustomer.getAge());
            //find addresses in repository to reuse address with same zip and number
            customer.setAddress(newCustomer.getAddress()
                .stream()
                .map(addressService::findAddress)
                .collect(Collectors.toSet()));
            customer.setLastUpdateInfo(new Date());
            return this.repository.save(customer);
        }).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + documentId + " not found."));
    }

    /**
     * Delete a customer from the repository.
     * Before deleting, we filter out addresses that are shared amongst other customers to prevent those from being removed
     * @param documentId identifier of the customer to be deleted
     * @throws ResponseStatusException if no customer with documentId parm is found
     */
    public void deleteCustomer(final String documentId) throws ResponseStatusException {
        Customer customer = this.repository.findById(documentId).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with id " + documentId + " not found."));
        customer.setAddress(customer.getAddress()
            .stream()
            .filter(c -> c.getCustomers().size() == 1)
            .collect(Collectors.toSet()));
        this.repository.delete(customer);
    }

}
