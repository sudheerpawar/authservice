package com.loontao.authservice.dto;

import lombok.Data;


@Data
public class RegisterUserDto {

    private String emailId;
    
    private String password;
    
    private String fullname;

    private String address;

    private String city;

    private String country;

    private String phoneNumber;

    private String pincode;

    private String dateOfBirth;

    private String firstName; 

    private String lastName;
 
    // getters and setters here...
}
