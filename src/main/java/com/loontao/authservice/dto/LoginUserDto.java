package com.loontao.authservice.dto;

import lombok.Data;

@Data
public class LoginUserDto {
    
    private String emailId;

    private String firstName;
    
    private String password;

    private String phoneNumber;
    
    // getters and setters here...
}
