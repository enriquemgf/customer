package com.bradesco.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bradesco.dto.AddressDto;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipCode;

    private String number;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "address")
    private Set<Customer> customers;


    public static Address fromAddressDto(final AddressDto addressDto){
        return builder()
            .zipCode(addressDto.getZipCode())
            .number(addressDto.getNumber())
            .build();
    }
}