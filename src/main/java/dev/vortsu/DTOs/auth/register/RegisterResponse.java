package dev.barapp.DTOs.auth.register;

import dev.barapp.entities.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterResponse {
    private String name;
    private String email;
    private Role role;
}
