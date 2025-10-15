package com.example.model;

public class User {
    private String recordId;
    private String email;
    private String password;
    private String name;
    private String role;
    private String contracttype;

    // الحقول الجديدة
    private String position;
    private String employeeImage;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name, String role, String contracttype, String position, String employeeImage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.contracttype = contracttype;
        this.position = position;
        this.employeeImage = employeeImage;
    }

    // Getters & Setters
    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContracttype() { return contracttype; }
    public void setContracttype(String contracttype) { this.contracttype = contracttype; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getEmployeeImage() { return employeeImage; }
    public void setEmployeeImage(String employeeImage) { this.employeeImage = employeeImage; }
}
