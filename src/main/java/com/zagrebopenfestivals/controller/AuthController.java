package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.request.LoginRequest;
import com.zagrebopenfestivals.dto.request.RegisterRequest;
import com.zagrebopenfestivals.dto.response.AuthResponse;
import com.zagrebopenfestivals.security.RefreshCookieService;
import com.zagrebopenfestivals.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshCookieService refreshCookieService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthService.LoginResult result = authService.login(request);
        refreshCookieService.set(response, result.rawRefreshToken());
        return result.response();
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String rawToken = refreshCookieService.read(request);
        AuthService.RefreshResult result = authService.refresh(rawToken);
        refreshCookieService.set(response, result.rawRefreshToken());
        return result.response();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String rawToken = refreshCookieService.read(request);
        authService.logout(rawToken);
        refreshCookieService.clear(response);
        return ResponseEntity.noContent().build();
    }
}
