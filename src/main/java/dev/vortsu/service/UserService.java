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
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegistrationRequest request) {
        // Проверяем, нет ли уже пользователя с таким username
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Создаем и сохраняем пароль
        Password password = new Password();
        password.setPassword(passwordEncoder.encode(request.getPassword()));
        Password savedPassword = passwordRepository.save(password);

        // Создаем пользователя
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setPassword(savedPassword);
        user.setEnable(true);

        return userRepository.save(user);
    }
}