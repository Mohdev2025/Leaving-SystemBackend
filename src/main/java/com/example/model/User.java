package com.example.model;

public class User {
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
