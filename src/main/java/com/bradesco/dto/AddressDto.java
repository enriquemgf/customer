package com.bradesco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.bradesco.customer.Address;
import com.bradesco.validator.ZipCode;

/**
 * Data Transfer Object for {@link Address} to hold validations and fail early
 * @author enrique.guijarro
 * @since Nov-2022
 */
@Data
@AllArgsConstructor
public class AddressDto {

    //adding id just to prove same address entry is being reused. Could be hidden as it's not useful
    private Long id;

    @ZipCode
    private String zipCode;

    @Pattern(regexp="[0-9]+", message="Address number should be numeric.")
    @NotBlank
    private String number;

    public static AddressDto fromAddress(final Address address){
        return new AddressDto(address.getId(), address.getZipCode(), address.getNumber());
    }

}
