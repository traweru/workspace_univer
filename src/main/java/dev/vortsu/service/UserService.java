package dev.vortsu.service;

import dev.vortsu.entity.User;
import dev.vortsu.entity.Password;
import dev.vortsu.dto.RegistrationRequest;
import dev.vortsu.repositories.UserRepository;
import dev.vortsu.repositories.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegistrationRequest request) {
        try {
            System.out.println("=== REGISTRATION START ===");
            System.out.println("Request: " + request.getUsername() + ", " + request.getEmail());

            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Username already exists");
            }

            Password password = new Password();
            password.setPassword(passwordEncoder.encode(request.getPassword()));
            Password savedPassword = passwordRepository.save(password);

            User user = new User();
            user.setUsername(request.getUsername());
            user.setRole(request.getRole());
            user.setPassword(savedPassword);
            user.setEnable(true);

            User savedUser = userRepository.save(user);
            userRepository.flush(); // ← принудительно сохраняем в БД
            System.out.println("USER FLUSHED TO DB: " + savedUser.getId());

            System.out.println("=== REGISTRATION SUCCESS ===");
            System.out.println("User ID: " + savedUser.getId());
            System.out.println("Username: " + savedUser.getUsername());


            return savedUser;

        } catch (Exception e) {
            System.out.println("=== REGISTRATION ERROR ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}