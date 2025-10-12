<<<<<<< HEAD
 package com.example.repository;
=======
package com.example.repository;
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3

import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
<<<<<<< HEAD
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;
=======
import java.util.Map;
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3

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
<<<<<<< HEAD
public User findByEmail(String userEmail) {
    try {
        String url = "https://api.airtable.com/v0/" + baseId + "/" + userTableId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);
=======

    public User findByUsername(String username) {
        String url = "https://api.airtable.com/v0/" + baseId + "/" + userTableId
                + "?filterByFormula={usernameFormula}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String formula = "({Username}='" + username + "')";
        url = url.replace("{usernameFormula}", formula);

>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("records")) {
<<<<<<< HEAD
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
=======
            var records = (java.util.List<Map<String, Object>>) body.get("records");
            if (!records.isEmpty()) {
                Map<String, Object> fields = (Map<String, Object>) records.get(0).get("fields");
                return new User(
                        (String) fields.get("Username"),
                        (String) fields.get("Password")
                );
            }
        }
        return null;
    }
>>>>>>> c8dc33f085b4f205b3e99d5e031b445c7234d9b3
}
