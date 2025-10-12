package com.example.model;

public class User {
<<<<<<< HEAD
    private String recordId;  // أضف هذا السطر
    private String email;
    private String password;
    private String name;
    private String role;
    private String contracttype;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String name, String role, String contracttype) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.contracttype = contracttype;
    }

    // Getters & Setters
    public String getRecordId() {
        return recordId;
    }
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getContracttype() {
        return contracttype;
    }
    public void setContracttype(String contracttype) {
        this.contracttype = contracttype;
    }
}
=======
    private String userid;
    private String username;
    private String password;
    private String email;
    private String contracttype;
    private String role;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters and setters
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContracttype() { return contracttype; }
    public void setContracttype(String contracttype) { this.contracttype = contracttype; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
