package com.ecom.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String addressId;
    @NotBlank(message = "address.userId must be present")
    private String userId;
    @NotBlank(message = "address.addressLine1 must be present")
    private String addressLine1;
    @NotBlank(message = "address.addressLine2 must be present")
    private String addressLine2;
    @NotBlank(message = "address.postalCode must be present")
    private String postalCode;
    @NotBlank(message = "address.city must be present")
    private String city;
    @NotBlank(message = "address.state must be present")
    private String state;
    @NotBlank(message = "address.country must be present")
    private String country;
    private Date createdAt = new Date();
    private Date modifiedAt = new Date();
}
