package com.bradesco.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bradesco.customer.Customer;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    Optional<List<Customer>> findByAddress_ZipCode(String zipCode);

}
