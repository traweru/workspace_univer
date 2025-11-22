package dev.vortsu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.vortsu.entity.User;
import dev.vortsu.entity.Password;
import dev.vortsu.entity.Role;
import dev.vortsu.repositories.UserRepository;
import dev.vortsu.repositories.PasswordRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.annotation.PostConstruct;

@Component
public class Initializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initial() {
        if (userRepository.count() == 0) {
            // Создаем пароль
            Password password = new Password();
            password.setPassword(passwordEncoder.encode("1234"));
            Password savedPassword = passwordRepository.save(password);

            // Создаем пользователя с помощью сеттеров
            User student = new User();
            student.setUsername("student");
            student.setRole(Role.STUDENT);
            student.setPassword(savedPassword);
            student.setEnable(true);

            userRepository.save(student);
            System.out.println("Initial user created successfully");
        }
    }
}