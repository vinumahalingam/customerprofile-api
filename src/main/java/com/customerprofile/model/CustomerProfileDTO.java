package com.customerprofile.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
