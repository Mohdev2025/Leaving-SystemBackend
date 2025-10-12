<<<<<<< HEAD
 package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
=======
package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
<<<<<<< HEAD
@CrossOrigin(origins = "http://localhost:4200")
=======
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
<<<<<<< HEAD
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
=======
    public String login(@RequestBody User user) {
        boolean isValid = service.validateUser(user.getUsername(), user.getPassword());
        if (isValid) {
            return "Login successful!";
        } else {
            return "Invalid username or password!";
        }
    }
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
}
