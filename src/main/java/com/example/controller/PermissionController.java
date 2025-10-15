package com.example.controller;

import com.example.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permission")
@CrossOrigin(origins = "http://localhost:4200")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    // 1️⃣ Get all permissions
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllPermissions() {
        List<Map<String, Object>> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

@PostMapping("/create")
public ResponseEntity<Map<String, Object>> createPermission(@RequestBody Map<String, String> requestBody) {
    String permissionType = requestBody.get("permissionType");
    String date = requestBody.get("date");
    String from = requestBody.get("from");
    String to = requestBody.get("to");
    String reason = requestBody.get("reason");
    String name = requestBody.get("name");   
    		String userId = requestBody.get("userId");
    String status = requestBody.getOrDefault("status", "Pending");

    // إنشاء البرميشن
    Map<String, Object> createdPermission = permissionService.createPermission(
        permissionType, date, from, to, reason,name,  userId ,status
    );

    // لو البيانات موجودة
    if (createdPermission != null && !createdPermission.isEmpty()) {
        Map<String, Object> response = Map.of(
            "message", "Permission created successfully",
            "data", createdPermission
        );
        return ResponseEntity.ok(response);
    } else {
        Map<String, Object> errorResponse = Map.of(
            "message", "Failed to create permission"
        );
        return ResponseEntity.status(500).body(errorResponse);
    }
}


    // 3️⃣ Get all permissions for specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getPermissionsByUser(@PathVariable String userId) {
        List<Map<String, Object>> permissions = permissionService.getPermissionsByUser(userId);
        return ResponseEntity.ok(permissions);
    }

    // 4️⃣ Update permission status
    @PatchMapping("/update-status/{permissionId}")
    public ResponseEntity<Map<String, String>> updatePermissionStatus(
            @PathVariable String permissionId,
            @RequestBody Map<String, String> requestBody) {

        String newStatus = requestBody.get("status");

        if (newStatus == null || newStatus.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Status field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        System.out.println("Updating permission: " + permissionId + " to status: " + newStatus);

        boolean success = permissionService.updatePermissionStatus(permissionId, newStatus);

        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "Permission status updated successfully");
            response.put("permissionId", permissionId);
            response.put("newStatus", newStatus);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update permission status");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 5️⃣ Delete permission
    @DeleteMapping("/delete/{permissionId}")
    public ResponseEntity<Map<String, String>> deletePermission(@PathVariable String permissionId) {
        System.out.println("Deleting permission: " + permissionId);

        boolean success = permissionService.deletePermission(permissionId);

        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "Permission deleted successfully");
            response.put("permissionId", permissionId);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to delete permission");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 6️⃣ Get single permission by ID
    @GetMapping("/{permissionId}")
    public ResponseEntity<Map<String, Object>> getPermissionById(@PathVariable String permissionId) {
        Map<String, Object> permission = permissionService.getPermissionById(permissionId);

        if (permission.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Permission not found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        return ResponseEntity.ok(permission);
    }
    
 // Add this method to PermissionController
    @GetMapping("/debug/fields")
    public ResponseEntity<Map<String, Object>> debugAvailableFields() {
        List<Map<String, Object>> permissions = permissionService.getAllPermissions();
        
        Map<String, Object> response = new HashMap<>();
        
        if (!permissions.isEmpty()) {
            Map<String, Object> firstRecord = permissions.get(0);
            response.put("message", "Available fields in Airtable Permission table:");
            response.put("fieldNames", firstRecord.keySet());
            response.put("sampleRecord", firstRecord);
        } else {
            response.put("message", "No records found in Permission table");
        }
        
        return ResponseEntity.ok(response);
    }
}