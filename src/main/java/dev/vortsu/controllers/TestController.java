package dev.vortsu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        return jdbcTemplate.queryForList("SELECT * FROM users");
    }

    @GetMapping("/passwords")
    public List<Map<String, Object>> getAllPasswords() {
        return jdbcTemplate.queryForList("SELECT * FROM passwords");
    }

    @GetMapping("/tables")
    public List<Map<String, Object>> getAllTables() {
        return jdbcTemplate.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES");
    }

    @GetMapping("/check")
    public String checkDatabase() {
        try {
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                    "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'"
            );
            return "Database connected. Tables: " + tables.toString();
        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }

}