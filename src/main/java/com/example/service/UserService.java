package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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
    }
}