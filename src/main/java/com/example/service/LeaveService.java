package com.example.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaveService {

    private final RestTemplate restTemplate;

    @Value("${airtable.api.key}")
    private String airtableToken;

    @Value("${airtable.base.id}")
    private String baseId;

    @Value("${airtable.table.leave}")
    private String leaveTableId;

    public LeaveService() {
        // Create RestTemplate with Apache HttpClient to support PATCH
        org.springframework.http.client.HttpComponentsClientHttpRequestFactory requestFactory = 
            new org.springframework.http.client.HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setConnectionRequestTimeout(5000);
        this.restTemplate = new RestTemplate(requestFactory);
        
        System.out.println("✅ RestTemplate initialized with Apache HttpClient for PATCH support");
    }

    // Generate next leave ID in format LV001, LV002...
    private String generateLeaveId() {
        List<Map<String, Object>> leaves = getAllLeaveRequests();
        int nextNumber = leaves.size() + 1;
        return String.format("LV%03d", nextNumber);
    }

    public boolean applyLeave(String userRecordId,
                              String typeRecordId,
                              String startTime,
                              String endTime,
                              String description,
                              String status) {

        String leaveId = generateLeaveId();

        System.out.println("=== DEBUG INFO ===");
        System.out.println("Leave ID: " + leaveId);
        System.out.println("User Record ID: " + userRecordId);
        System.out.println("Type Record ID: " + typeRecordId);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Description: " + description);
        System.out.println("Status: " + status);

        String url = "https://api.airtable.com/v0/" + baseId + "/" + leaveTableId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + airtableToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> fields = new HashMap<>();
        fields.put("leave id", leaveId);
        fields.put("User (userid)", new String[]{userRecordId});
        fields.put("type id", new String[]{typeRecordId});
        fields.put("start time", startTime);
        fields.put("end time", endTime);
        fields.put("description", description);
        fields.put("status", status);

        Map<String, Object> body = new HashMap<>();
        body.put("fields", fields);

        System.out.println("Request Body: " + body);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
            );
            System.out.println("✅ Status: " + response.getStatusCode());
            System.out.println("✅ Response: " + response.getBody());
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.out.println("❌ Full Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getAllLeaveRequests() {
        try {
            String url = "https://api.airtable.com/v0/" + baseId + "/" + leaveTableId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + airtableToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> leaveRequests = new ArrayList<>();

            if (body != null && body.containsKey("records")) {
                List<Map<String, Object>> records = (List<Map<String, Object>>) body.get("records");

                for (Map<String, Object> record : records) {
                    Map<String, Object> leaveRequest = new HashMap<>();
                    leaveRequest.put("id", record.get("id"));
                    leaveRequest.put("fields", record.get("fields"));
                    leaveRequests.add(leaveRequest);
                }
            }
            return leaveRequests;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    // Helper method to find record ID by leave ID (LV001, LV002, etc.)
    private String findRecordIdByLeaveId(String leaveId) {
        List<Map<String, Object>> allLeaves = getAllLeaveRequests();
        
        for (Map<String, Object> leave : allLeaves) {
            Map<String, Object> fields = (Map<String, Object>) leave.get("fields");
            if (fields != null && fields.containsKey("leave id")) {
                String recordLeaveId = fields.get("leave id").toString();
                if (leaveId.equals(recordLeaveId)) {
                    return leave.get("id").toString();
                }
            }
        }
        return null;
    }
    
    public boolean updateLeaveStatus(String leaveIdOrRecordId, String newStatus) {
        try {
            // Check if it's a record ID (starts with "rec") or a leave ID (starts with "LV")
            String recordId;
            if (leaveIdOrRecordId.startsWith("rec")) {
                recordId = leaveIdOrRecordId;
                System.out.println("✅ Using Airtable record ID: " + recordId);
            } else {
                // It's a leave ID, need to find the record ID
                recordId = findRecordIdByLeaveId(leaveIdOrRecordId);
                if (recordId == null) {
                    System.out.println("❌ Leave ID not found: " + leaveIdOrRecordId);
                    return false;
                }
                System.out.println("✅ Found record ID " + recordId + " for leave ID: " + leaveIdOrRecordId);
            }
            
            String url = "https://api.airtable.com/v0/" + baseId + "/" + leaveTableId + "/" + recordId;
            System.out.println("✅ Request URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + airtableToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> fields = new HashMap<>();
            fields.put("status", newStatus);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("fields", fields);

            System.out.println("✅ Request Body: " + requestBody);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Now PATCH will work because we configured RestTemplate with Apache HttpClient
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, Map.class);
            
            System.out.println("✅ Response Status: " + response.getStatusCode());
            System.out.println("✅ Response Body: " + response.getBody());
            
            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            System.out.println("❌ Error updating leave status:");
            e.printStackTrace();
            return false;
        }
    }
}