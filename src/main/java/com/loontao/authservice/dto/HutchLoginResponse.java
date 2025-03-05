package com.loontao.authservice.dto;

import lombok.Data;

@Data
public class HutchLoginResponse {
    private String accessToken;
    private String refreshToken;
}
