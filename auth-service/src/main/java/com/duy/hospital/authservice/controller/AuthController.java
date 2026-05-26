package com.duy.hospital.authservice.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.duy.hospital.authservice.dto.request.LoginRequest;
import com.duy.hospital.authservice.dto.request.LogoutRequest;
import com.duy.hospital.authservice.dto.request.RefreshTokenRequest;
import com.duy.hospital.authservice.dto.response.AuthResponse;
import com.duy.hospital.authservice.dto.response.MessageResponse;
import com.duy.hospital.authservice.dto.response.UserResponse;
import com.duy.hospital.authservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);
        return new MessageResponse("Logged out successfully");
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return authService.me(jwt);
    }
}
