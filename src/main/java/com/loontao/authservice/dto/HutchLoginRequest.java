package com.loontao.authservice.dto;

import lombok.Data;

@Data
public class HutchLoginRequest {
    private String username;
    private String password;
}
