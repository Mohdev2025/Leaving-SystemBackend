 package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = service.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (user != null) {
            String token = generateSimpleToken(user);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getRecordId());
            userInfo.put("email", user.getEmail());
            userInfo.put("name", user.getName());
            userInfo.put("role", user.getRole());
            userInfo.put("contracttype", user.getContracttype());
            userInfo.put("position", user.getPosition()); // الحقل الجديد
            userInfo.put("employeeImage", user.getEmployeeImage()); // الحقل الجديد

            response.put("user", userInfo);


            return ResponseEntity.ok(response);

        } else {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Wrong email or password!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // 🔹 Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        // هنا يمكن تنظيف أي بيانات session إذا كانت موجودة
        // حالياً مجرد إرجاع رسالة نجاح
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    private String generateSimpleToken(User user) {
        return "Bearer_" + user.getEmail() + "_" + System.currentTimeMillis();
    }
}