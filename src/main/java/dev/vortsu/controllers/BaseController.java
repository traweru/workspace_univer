package dev.vortsu.controllers;

import dev.vortsu.dto.Student;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/base")
public class BaseController {

    private Long counter = 0L;

    private Long genetareId(){ return counter++ ;}

    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    private void init(){
        students.add(new Student(0L,"user1","VM","+7"));
        students.add(new Student(1L,"user2","VM","+8"));
        students.add(new Student(2L,"user3","dM","+99"));
    }

    @PostMapping(value ="Students", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student createStudent (@RequestBody Student newStudent){return addStudent(newStudent);}

    @PutMapping(value = "students", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student updateStudent (@RequestBody Student changingStudent){
        return updateStudentInterval(changingStudent);
    }

    private Student updateStudentInterval(Student student){
        if (student.getId() == null){
            throw new RuntimeException("id of changing student cannot be null");
        }

        Student changingStudent = students.stream()
                .filter(el -> Objects.equals(el.getId(), student.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("student with id " + student.getId() + "was not found"));

        changingStudent.setFio(student.getFio());
        changingStudent.setGroup(student.getGroup());
        changingStudent.setPhoneNumber(student.getPhoneNumber());

        return student;

    }
    private Student addStudent(Student student){
        student.setId(genetareId());
        students.add(student);
        return student;
    }
    @GetMapping("getAllStudents")
    public List<Student>getAllStudents(){
        return students;
    }
    @GetMapping("check")
    public String greetJava(){
        return "Hello world" + new Date();
    }
    @GetMapping(value = "students/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Student> filterStudentsByGroup(@RequestParam(value = "group") String group) {
        List<Student> filteredStudents = students.stream()
                .filter(el -> el.getGroup().equals(group))
                .collect(Collectors.toList());  // ← Собираем ВСЕХ в список!

        if (filteredStudents.isEmpty()) {
            throw new RuntimeException("Студенты в группе '" + group + "' не найдены");
        }

        return filteredStudents;
    }
    @GetMapping(value ="students/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public Student getStudentById(@PathVariable("id")Long id){
        return students.stream()
                .filter(el->el.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new RuntimeException("student with id:"+ id + "was not found"));
    }
   @DeleteMapping(value = "students/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
            public Long deleteStudent(@PathVariable("id")Long id){
        return removeStudent(id);
    }
    private Long removeStudent(Long id){
        students.removeIf(el ->el.getId().equals(id));
        return id;
    }

}
