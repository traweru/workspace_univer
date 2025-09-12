package dev.barapp.controllers;

import dev.barapp.DTOs.auth.login.LoginRequest;
import dev.barapp.DTOs.auth.login.LoginResponse;
import dev.barapp.DTOs.auth.register.RegisterRequest;
import dev.barapp.DTOs.auth.register.RegisterResponse;
import dev.barapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }

}
