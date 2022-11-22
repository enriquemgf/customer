package com.bradesco.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bradesco.customer.Address;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByZipCodeAndNumber(String zipCode, String number);
}
