package com.zagrebopenfestivals.service;

import com.zagrebopenfestivals.dto.request.LoginRequest;
import com.zagrebopenfestivals.dto.request.RegisterRequest;
import com.zagrebopenfestivals.dto.response.AuthResponse;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResult login(LoginRequest request);

    RefreshResult refresh(String rawToken);

    void logout(String rawToken);

    record LoginResult(AuthResponse response, String rawRefreshToken) {
    }

    record RefreshResult(AuthResponse response, String rawRefreshToken) {
    }
}