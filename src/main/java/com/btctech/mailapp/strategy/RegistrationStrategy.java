package com.btctech.mailapp.strategy;

import com.btctech.mailapp.dto.RegisterRequest;
import com.btctech.mailapp.entity.User;

public interface RegistrationStrategy {
    User register(RegisterRequest request);
    String getMode();
}
