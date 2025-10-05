package dev.vortsu.dto;

import dev.vortsu.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
    private Role role; // STUDENT, TEACHER, ADMIN
}