package dev.vortsu.dto;

public class Student {
    private Long id;
    private String fio;
    private String group;
    private String phoneNumber;

    // Конструкторы
    public Student() {}

    public Student(Long id, String fio, String group, String phoneNumber) {
        this.id = id;
        this.fio = fio;
        this.group = group;
        this.phoneNumber = phoneNumber;
    }

    public Student(String fio, String group, String phoneNumber) {
        this.fio = fio;
        this.group = group;
        this.phoneNumber = phoneNumber;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFio() { return fio; }
    public void setFio(String fio) { this.fio = fio; } //

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; } //

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; } //
}