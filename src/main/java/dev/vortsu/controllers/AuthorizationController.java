package dev.vortsu.controllers;

import dev.vortsu.dto.LoginRequest;
import dev.vortsu.dto.RegistrationRequest;
import dev.vortsu.entity.User;
import dev.vortsu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class AuthorizationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/register")

    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        System.out.println("=== REGISTER ENDPOINT CALLED ===");
        System.out.println("Username: " + registrationRequest.getUsername());
        System.out.println("Email: " + registrationRequest.getEmail());
        System.out.println("Role: " + registrationRequest.getRole());

        try {
            User user = userService.registerUser(registrationRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", user.getUsername());
            response.put("userid", user.getId()); // Обратите внимание - userid (не userId)

            System.out.println("=== SENDING RESPONSE ===");
            System.out.println("Response: " + response);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("=== REGISTER ERROR ===");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Registration failed", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", authentication.getName());
            response.put("roles", roles);
            response.put("authenticated", true);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("error", "Login failed", "message", "Invalid username or password")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Login failed", "message", "Internal server error")
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser")) {

            String roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("username", authentication.getName());
            response.put("roles", roles);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(Map.of("authenticated", false));
    }
}