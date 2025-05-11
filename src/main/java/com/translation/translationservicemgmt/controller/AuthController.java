package com.translation.translationservicemgmt.controller;

import com.translation.translationservicemgmt.dto.AuthResponse;
import com.translation.translationservicemgmt.dto.LoginRequest;
import com.translation.translationservicemgmt.dto.RegisterRequestDto;
import com.translation.translationservicemgmt.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDto request) {
        String token = authService.register(request);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }
}
