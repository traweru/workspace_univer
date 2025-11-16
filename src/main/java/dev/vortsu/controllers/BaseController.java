package dev.vortsu.controllers;

import dev.vortsu.dto.Student;
import dev.vortsu.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize; // ← добавили
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/base")
@PreAuthorize("hasAuthority('ADMIN')") // ← ВСЕ методы доступны ТОЛЬКО админу
public class BaseController {

    private final StudentRepository studentRepository;

    @Autowired
    public BaseController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Создание нового студента
    @PostMapping(value = "students", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student createStudent(@RequestBody Student newStudent) {
        return studentRepository.save(newStudent);
    }

    // Обновление существующего студента
    @PutMapping(value = "students", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student updateStudent(@RequestBody Student changingStudent) {
        if (changingStudent.getId() == null) {
            throw new RuntimeException("ID изменяемого студента не может быть null");
        }
        return studentRepository.save(changingStudent);
    }

    // Получение всех студентов — разрешим и студентам!
    @GetMapping("getAllStudents")
    @PreAuthorize("permitAll()") // ← студенты тоже могут читать
    public List<Student> getAllStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    // Простой тестовый endpoint
    @GetMapping("check")
    @PreAuthorize("permitAll()")
    public String greetJava() {
        return "Hello world " + new Date();
    }

    // Фильтрация студентов по группе — разрешим всем
    @GetMapping(value = "students/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll()")
    public List<Student> filterStudentsByGroup(@RequestParam(value = "group") String group) {
        List<Student> allStudents = (List<Student>) studentRepository.findAll();
        List<Student> filteredStudents = allStudents.stream()
                .filter(student -> student.getGroup().equals(group))
                .collect(Collectors.toList());

        if (filteredStudents.isEmpty()) {
            throw new RuntimeException("Студенты в группе '" + group + "' не найдены");
        }

        return filteredStudents;
    }

    // Получение студента по ID — разрешим всем
    @GetMapping(value = "students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("permitAll()")
    public Student getStudentById(@PathVariable("id") Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Студент с id: " + id + " не найден"));
    }

    // Удаление студента по ID
    @DeleteMapping(value = "students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteStudent(@PathVariable("id") Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Студент с id: " + id + " не найден");
        }
        studentRepository.deleteById(id);
        return "Студент с id " + id + " успешно удален";
    }
}