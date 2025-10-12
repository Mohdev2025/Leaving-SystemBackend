package com.example.service;

import com.example.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // 1️⃣ Get all permissions
    public List<Map<String, Object>> getAllPermissions() {
        return permissionRepository.getAllPermissions();
    }

    // 2️⃣ Create new permission with status
    public Map<String, Object> createPermission(
            String permissionType,
            String date,
            String from,
            String to,
            String reason,
            String userId,
            String status
    ) {
        Map<String, Object> fields = Map.of(
            "PermissionType", permissionType,
            "Date", date,
            "From", from,
            "To", to,
            "Reason", reason,
            "User (userid)", List.of(userId),
            "status", status != null ? status : "Pending"
        );
        return permissionRepository.createPermission(fields);
    }

    // 2️⃣ Overloaded method without status (defaults to "Pending")
    public Map<String, Object> createPermission(
            String permissionType,
            String date,
            String from,
            String to,
            String reason,
            String userId
          
    ) {
        return createPermission(permissionType, date, from, to, reason, userId, "Pending");
    }

    // 3️⃣ Get permissions for specific user
    public List<Map<String, Object>> getPermissionsByUser(String userId) {
        return permissionRepository.getPermissionsByUser(userId);
    }

    // 4️⃣ Update permission status
    public boolean updatePermissionStatus(String permissionId, String newStatus) {
        return permissionRepository.updatePermissionStatus(permissionId, newStatus);
    }

    // 5️⃣ Delete permission
    public boolean deletePermission(String permissionId) {
        return permissionRepository.deletePermission(permissionId);
    }

    // 6️⃣ Get single permission by ID
    public Map<String, Object> getPermissionById(String permissionId) {
        return permissionRepository.getPermissionById(permissionId);
    }
}