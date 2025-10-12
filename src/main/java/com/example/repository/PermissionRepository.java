package com.example.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Repository
public class PermissionRepository {
    
    // ✅ FIXED: Configure RestTemplate with HttpComponentsClientHttpRequestFactory to support PATCH
    private final RestTemplate restTemplate;

    @Value("${airtable.api.key}")
    private String airtableToken;

    @Value("${airtable.base.id}")
    private String baseId;

    @Value("${airtable.table.Permission}")
    private String tablePermission;

    // Constructor to initialize RestTemplate with proper configuration
    public PermissionRepository() {
        this.restTemplate = new RestTemplate();
        // Use HttpComponentsClientHttpRequestFactory to support PATCH method
        this.restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    private String getAirtableUrl() {
        return "https://api.airtable.com/v0/" + baseId + "/" + tablePermission;
    }

    // 1️⃣ Get all permissions
    public List<Map<String, Object>> getAllPermissions() {
        String url = getAirtableUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + airtableToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<Map<String, Object>> records = (List<Map<String, Object>>) response.getBody().get("records");
                List<Map<String, Object>> result = new ArrayList<>();

                for (Map<String, Object> record : records) {
                    Map<String, Object> fields = (Map<String, Object>) record.get("fields");
                    fields.put("id", record.get("id"));
                    result.add(fields);
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Error fetching permissions from Airtable:");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    // 2️⃣ Create new permission
 public Map<String, Object> createPermission(Map<String, Object> fields) {
    String url = getAirtableUrl();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + airtableToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    Map<String, Object> requestBody = Map.of("fields", fields);
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    try {
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        // ✅ تعديل: قبول أي 2xx بدلاً من HttpStatus.CREATED فقط
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> record = response.getBody();
            Map<String, Object> result = (Map<String, Object>) record.get("fields");
            result.put("id", record.get("id")); // إضافة معرف السجل
            System.out.println("✅ Permission created successfully: " + result);
            return result;
        } else {
            System.out.println("Airtable response status: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
        }
    } catch (Exception e) {
        System.err.println("❌ Error creating permission in Airtable:");
        e.printStackTrace();
    }

    return Collections.emptyMap();
}


    // 3️⃣ Get permissions for specific user
    public List<Map<String, Object>> getPermissionsByUser(String userId) {
        List<Map<String, Object>> allPermissions = getAllPermissions();
        List<Map<String, Object>> userPermissions = new ArrayList<>();

        for (Map<String, Object> perm : allPermissions) {
            List<String> users = (List<String>) perm.get("User (userid)");
            if (users != null && users.contains(userId)) {
                userPermissions.add(perm);
            }
        }
        return userPermissions;
    }

    // 4️⃣ Update permission status (NOW WORKS WITH PATCH!)
    public boolean updatePermissionStatus(String permissionId, String newStatus) {
        String url = getAirtableUrl() + "/" + permissionId;

        // Try multiple possible field name variations
        String[] possibleFieldNames = {
            "Status",           // Standard
            "status",           // Lowercase
            "PermissionStatus", // Combined
            "Permission Status",// With space
            "State",            // Alternative
            "state"             // Alternative lowercase
        };

        for (String fieldName : possibleFieldNames) {
            try {
                Map<String, Object> fields = Map.of(fieldName, newStatus);
                Map<String, Object> body = Map.of("fields", fields);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + airtableToken);

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

                ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    request,
                    Map.class
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    System.out.println("✅ Permission status updated using field '" + fieldName + "': " + permissionId + " -> " + newStatus);
                    return true;
                }

            } catch (Exception e) {
                // Field name didn't work, try next one
                System.out.println("⚠️ Field name '" + fieldName + "' not found, trying next...");
            }
        }

        // If none worked, log available fields
        System.err.println("❌ CRITICAL: Status field not found! Please check available fields:");
        debugFieldNames();
        return false;
    }

    // 5️⃣ Delete permission
    public boolean deletePermission(String permissionId) {
        try {
            String url = getAirtableUrl() + "/" + permissionId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + airtableToken);

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                request,
                Map.class
            );

            System.out.println("✅ Permission deleted: " + permissionId);
            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            System.err.println("❌ Error deleting permission: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 6️⃣ Get single permission by ID
    public Map<String, Object> getPermissionById(String permissionId) {
        try {
            String url = getAirtableUrl() + "/" + permissionId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + airtableToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> record = response.getBody();
                Map<String, Object> fields = (Map<String, Object>) record.get("fields");
                fields.put("id", record.get("id"));
                return fields;
            }
        } catch (Exception e) {
            System.err.println("❌ Error fetching permission by ID:");
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    // 🔍 DEBUG: Print all field names from a permission record
    public void debugFieldNames() {
        List<Map<String, Object>> permissions = getAllPermissions();
        if (!permissions.isEmpty()) {
            Map<String, Object> firstPermission = permissions.get(0);
            System.out.println("=== AIRTABLE FIELD NAMES ===");
            for (String fieldName : firstPermission.keySet()) {
                System.out.println("Field: \"" + fieldName + "\" = " + firstPermission.get(fieldName));
            }
            System.out.println("============================");
        }
    }
}