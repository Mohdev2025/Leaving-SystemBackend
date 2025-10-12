package com.example.service;

import com.example.model.User;
<<<<<<< HEAD
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
=======
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3

@Service
public class UserService {

<<<<<<< HEAD
    private final UserRepository userRepository;

    // Constructor injection (best practice in Spring)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticate user by email and password.
     * Returns the full User object (including name, role, contracttype)
     * if credentials are valid, otherwise returns null.
     *
     * @param email    user's email
     * @param password user's password
     * @return User with all details if valid; otherwise null
     */
    public User authenticate(String email, String password) {
        // Fetch user from Airtable
        User user = userRepository.findByEmail(email);

        // Validate credentials
        if (user != null && password.equals(user.getPassword())) {
            // User is valid, return full object with all fields
            return user;
        }

        // Authentication failed
        return null;
    }

    /**
     * Fetch a user by email without password validation.
     * Useful for profile pages or admin functions.
     *
     * @param email user's email
     * @return User object (with all fields) or null if not found
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
=======
    private final RestTemplate restTemplate;

    @Value("${airtable.api.key}")
    private String airtableToken;

    @Value("${airtable.base.id}")
    private String baseId;

    @Value("${airtable.table.user}")
    private String userTableId; // tbljvvKlwRmBMXc0z

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateUser(String username, String password) {
        String url = "https://api.airtable.com/v0/" + baseId + "/" + userTableId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + airtableToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                var records = (Iterable<Map<String, Object>>) body.get("records");

                for (Map<String, Object> record : records) {
                    Map<String, String> fields = (Map<String, String>) record.get("fields");
                    if (fields.get("Username").equals(username) && fields.get("Password").equals(password)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
    }
}
