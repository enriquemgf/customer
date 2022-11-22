package com.bradesco.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.bradesco.dto.CustomerDto;

/**
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    private String id;

    private String name;

    private String age;

    @EqualsAndHashCode.Exclude
    private Date registrationDate;

    @EqualsAndHashCode.Exclude
    private Date lastUpdateInfo;

    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "customer_address",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "addressId"))
    private Set<Address> address;

    public static Customer fromCustomerDto(final CustomerDto customerDto){
        return builder()
            .id(customerDto.getDocumentId())
            .name(customerDto.getName())
            .age(customerDto.getAge())
            .registrationDate(new Date())
            .lastUpdateInfo(new Date())
            .address(customerDto.getAddress()
                .stream()
                .map(Address::fromAddressDto)
                .collect(Collectors.toSet()))
            .build();
    }

}
