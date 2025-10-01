package com.example.service;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Service
public class UserService {

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
    }
}
