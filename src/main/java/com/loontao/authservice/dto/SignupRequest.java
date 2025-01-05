package com.loontao.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String userName;

    @NotBlank
    @Size(min = 10, max = 10)
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String pincode;

    @NotBlank
    @Size(min = 6)
    private String password;
}
