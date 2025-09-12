package dev.barapp.service;

import dev.barapp.DTOs.auth.register.RegisterRequest;
import dev.barapp.DTOs.auth.register.RegisterResponse;
import dev.barapp.DTOs.manager.ManagerRegisterWaiterDTO;
import dev.barapp.entities.*;
import dev.barapp.DTOs.auth.login.LoginResponse;
import dev.barapp.entities.enums.Role;
import dev.barapp.exceptions.UserAlreadyExistsException;
import dev.barapp.repositories.*;
import dev.barapp.security.JwtIssuer;
import dev.barapp.security.CredentialPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final WaiterRepository waiterRepository;
    private final CredentialRepository credentialRepository;

    public LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (CredentialPrincipal)authentication.getPrincipal();

        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principal.getCredentialId(), principal.getEmail(), roles);


        return LoginResponse.builder()
                .accessToken(token)
                .data(getAuthorizedUser(principal))
                .build();
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if(!credentialRepository.existsByEmail(registerRequest.getEmail())) {
            CredentialEntity credential = CredentialEntity.builder()
                    .role(Role.USER)
                    .email(registerRequest.getEmail())
                    .password(registerRequest.getPassword())
                    .build();

            UserEntity user = UserEntity.builder()
                    .credentialEntity(credential)
                    .name(registerRequest.getName())
                    .build();

            credentialRepository.save(credential);
            userRepository.save(user);

            return RegisterResponse.builder()
                    .role(Role.USER)
                    .email(registerRequest.getEmail())
                    .name(registerRequest.getName())
                    .build();
        }
        else{
            throw new UserAlreadyExistsException("Email already exists!");
        }
    }

    private <T extends BaseUserEntity> T getAuthorizedUser(CredentialPrincipal principal) {
        return switch (principal.getRole()) {
            case USER -> (T) userRepository.findUserEntityByCredentialEntity_Id(principal.getCredentialId());
            case MANAGER -> (T) managerRepository.findManagerEntityByCredentialEntity_Id(principal.getCredentialId());
            case WAITER -> (T) waiterRepository.findWaiterEntityByCredentialEntity_Id(principal.getCredentialId());
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
