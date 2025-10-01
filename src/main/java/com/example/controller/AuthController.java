package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        boolean isValid = service.validateUser(user.getUsername(), user.getPassword());
        if (isValid) {
            return "Login successful!";
        } else {
            return "Invalid username or password!";
        }
    }
}
