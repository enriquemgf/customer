package com.bradesco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bradesco.customer.Address;
import com.bradesco.dao.AddressRepository;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Service
public class AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressService(final AddressRepository addressRepository){
        this.repository = addressRepository;
    }

    /**
     * Returns an address from repository if found by zip code and number, otherwise return itself to be created in repo
     */
    public Address findAddress(Address address){
        return this.repository.findByZipCodeAndNumber(address.getZipCode(), address.getNumber()).orElse(address);
    }

}
