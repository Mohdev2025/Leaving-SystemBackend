 package com.example.repository;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

@Repository
public class UserRepository {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseId;
    private final String userTableId;

    public UserRepository(RestTemplate restTemplate,
                          @Value("${airtable.api.key}") String apiKey,
                          @Value("${airtable.base.id}") String baseId,
                          @Value("${airtable.table.user}") String userTableId) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseId = baseId;
        this.userTableId = userTableId;
    }
public User findByEmail(String userEmail) {
    try {
        String url = "https://api.airtable.com/v0/" + baseId + "/" + userTableId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("records")) {
            List<Map<String, Object>> records = (List<Map<String, Object>>) body.get("records");
            
            for (Map<String, Object> record : records) {
                Map<String, Object> fields = (Map<String, Object>) record.get("fields");
                String email = (String) fields.get("userEmail");
                
                if (email != null && email.equals(userEmail)) {
                    String password = (String) fields.get("Password");
                    String name = (String) fields.get("name");
                    String role = (String) fields.get("role");
                    String contracttype = (String) fields.get("contracttype");
                    
                    User user = new User(email, password, name, role, contracttype);
                    
                    // هذا السطر مهم جداً
                    String recordId = (String) record.get("id");
                    user.setRecordId(recordId);
                    
                    System.out.println("DEBUG - Record ID: " + recordId); // عشان تتأكد
                    
                    return user;
                }
            }
        }
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}
}