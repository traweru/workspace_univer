package dev.vortsu.service;

import dev.vortsu.dto.RegistrationRequest;
import dev.vortsu.entity.Password;
import dev.vortsu.entity.Role;
import dev.vortsu.entity.User;
import dev.vortsu.repositories.PasswordRepository;
import dev.vortsu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(RegistrationRequest request) {
        // Проверяем, существует ли пользователь с таким username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Преобразуем строку в enum Role
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + request.getRole() +
                    ". Available roles: STUDENT, TEACHER, ADMIN");
        }

        // Создаем пароль
        Password password = new Password();
        password.setPassword(passwordEncoder.encode(request.getPassword()));
        Password savedPassword = passwordRepository.save(password);

        // Создаем пользователя
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(savedPassword);
        user.setEnable(true);
        user.setRole(role);

        User savedUser = userRepository.save(user);

        System.out.println("USER FLUSHED TO DB: " + savedUser.getId());
        System.out.println("User ID: " + savedUser.getId());

        return savedUser;
    }
}