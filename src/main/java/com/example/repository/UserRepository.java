package com.example.repository;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

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

    public User findByUsername(String username) {
        String url = "https://api.airtable.com/v0/" + baseId + "/" + userTableId
                + "?filterByFormula={usernameFormula}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String formula = "({Username}='" + username + "')";
        url = url.replace("{usernameFormula}", formula);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("records")) {
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
}
