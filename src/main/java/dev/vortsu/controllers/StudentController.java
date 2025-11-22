package dev.vortsu.controllers;

import dev.vortsu.entity.Student;
import dev.vortsu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        System.out.println("GET /api/students - returning all students");
        return studentService.getAllStudents();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Student createStudent(@RequestBody Student student) {
        System.out.println("POST /api/students - creating student: " + student.getName());
        return studentService.saveStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        System.out.println("GET /api/students/" + id);
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        System.out.println("PUT /api/students/" + id);
        return studentService.getStudentById(id)
                .map(student -> {
                    student.setName(studentDetails.getName());
                    student.setGrade(studentDetails.getGrade());
                    student.setAttendance(studentDetails.getAttendance());
                    student.setAssignments(studentDetails.getAssignments());
                    student.setRating(studentDetails.getRating());
                    return ResponseEntity.ok(studentService.saveStudent(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        System.out.println("DELETE /api/students/" + id);
        if (studentService.getStudentById(id).isPresent()) {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStudents(@RequestBody List<Long> ids) {
        System.out.println("DELETE /api/students - deleting multiple: " + ids);
        for (Long id : ids) {
            if (studentService.getStudentById(id).isPresent()) {
                studentService.deleteStudent(id);
            }
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public String check() {
        return "API is working!";
    }
}