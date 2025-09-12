package dev.barapp.DTOs.auth.register;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
}
