package com.bradesco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bradesco.customer.Customer;

/**
 * Data Transfer Object for {@link Customer} to hold validations and fail early
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Data
@AllArgsConstructor
public class CustomerDto {

    //wasn't sure if document ID could be used as Primary Key, so I did.
    //alternatively, would have a new auto generated id as PK, then during insert verify uniqueness
    @NotBlank
    private String documentId;

    @NotBlank
    private String name;

    @NotBlank
    private String age;

    private String registrationDate;

    private String lastUpdateInfo;

    @Valid
    @NotNull
    private List<AddressDto> address;

    public static CustomerDto fromCustomer(final Customer customer){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return new CustomerDto(
            customer.getId(),
            customer.getName(),
            customer.getAge(),
            df.format(customer.getRegistrationDate()),
            df.format(customer.getLastUpdateInfo()),
            customer.getAddress().stream().map(AddressDto::fromAddress).collect(Collectors.toList()));

    }

}
