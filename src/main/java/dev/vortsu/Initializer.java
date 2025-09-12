package dev.vortsu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.vortsu.enitity.User;
import dev.vortsu.enitity.Password;
import dev.vortsu.enitity.Role;
import dev.vortsu.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class Initializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initial() {
        if (userRepository.count() == 0) {
            User student = new User(
                    null,
                    "student",
                    Role.STUDENT,
                    new Password(null, passwordEncoder.encode("1234")),
                    true
            );
            userRepository.save(student);
            System.out.println("Initial user created successfully");
        }
    }
}