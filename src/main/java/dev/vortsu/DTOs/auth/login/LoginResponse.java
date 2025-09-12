package dev.barapp.DTOs.auth.login;

import dev.barapp.entities.BaseUserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse<T extends BaseUserEntity> {
    private final String accessToken;
    private T data;
}
