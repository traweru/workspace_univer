package dev.vortsu.config;

import dev.vortsu.entity.Password;
import dev.vortsu.entity.Role;
import dev.vortsu.entity.Student;
import dev.vortsu.entity.User;
import dev.vortsu.repositories.PasswordRepository;
import dev.vortsu.repositories.StudentRepository;
import dev.vortsu.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        createTestUsers();


        createTestStudents();
    }

    private void createTestUsers() {
        if (userRepository.count() == 0) {
            System.out.println("Creating test users...");


            String encodedPassword = passwordEncoder.encode("password");


            Password adminPassword = new Password();
            adminPassword.setPassword(encodedPassword);
            passwordRepository.save(adminPassword);

            Password studentPassword = new Password();
            studentPassword.setPassword(encodedPassword);
            passwordRepository.save(studentPassword);

            Password teacherPassword = new Password();
            teacherPassword.setPassword(encodedPassword);
            passwordRepository.save(teacherPassword);


            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(adminPassword);
            admin.setEnable(true);
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            User student = new User();
            student.setUsername("student");
            student.setPassword(studentPassword);
            student.setEnable(true);
            student.setRole(Role.STUDENT);
            userRepository.save(student);

            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(teacherPassword);
            teacher.setEnable(true);
            teacher.setRole(Role.TEACHER);
            userRepository.save(teacher);

            System.out.println("Test users created successfully!");
        }
    }

    private void createTestStudents() {
        if (studentRepository.count() == 0) {
            System.out.println("Creating test students...");

            Student student1 = new Student();
            student1.setName("Иванов Иван");
            student1.setGrade(4);
            student1.setAttendance(85);
            student1.setAssignments(8);
            student1.setRating(4.2);

            Student student2 = new Student();
            student2.setName("Петрова Мария");
            student2.setGrade(5);
            student2.setAttendance(95);
            student2.setAssignments(10);
            student2.setRating(4.8);

            Student student3 = new Student();
            student3.setName("Сидоров Алексей");
            student3.setGrade(3);
            student3.setAttendance(75);
            student3.setAssignments(6);
            student3.setRating(3.5);

            Student student4 = new Student();
            student4.setName("Козлова Анна");
            student4.setGrade(5);
            student4.setAttendance(90);
            student4.setAssignments(9);
            student4.setRating(4.6);

            studentRepository.saveAll(Arrays.asList(student1, student2, student3, student4));

            System.out.println("Test students created successfully!");
        }
    }
}