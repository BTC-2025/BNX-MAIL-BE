package com.btctech.mailapp.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
