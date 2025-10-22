package com.example.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LeaveTypeRepository {
    
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseId;
    private final String leaveTypeTableId;

    public LeaveTypeRepository(RestTemplate restTemplate,
                               @Value("${airtable.api.key}") String apiKey,
                               @Value("${airtable.base.id}") String baseId,
                               @Value("${airtable.table.leavetype}") String leaveTypeTableId) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;                                          
        this.baseId = baseId;
        this.leaveTypeTableId = leaveTypeTableId;
    }

    public List<Map<String, String>> getAllLeaveTypes() {
        try {
            String url = "https://api.airtable.com/v0/" + baseId + "/" + leaveTypeTableId;

            HttpHeaders headers = new HttpHeaders(); 
            headers.set("Authorization", "Bearer " + apiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();
            List<Map<String, String>> leaveTypes = new ArrayList<>();

            if (body != null && body.containsKey("records")) {
                List<Map<String, Object>> records = (List<Map<String, Object>>) body.get("records");
                
                for (Map<String, Object> record : records) {
                    Map<String, String> leaveType = new HashMap<>();
                    leaveType.put("id", (String) record.get("id"));
                    
                    Map<String, Object> fields = (Map<String, Object>) record.get("fields");
                    leaveType.put("name", (String) fields.get("name"));
                    
                    leaveTypes.add(leaveType);
                }
            }
            return leaveTypes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}