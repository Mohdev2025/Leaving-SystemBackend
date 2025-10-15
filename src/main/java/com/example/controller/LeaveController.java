package com.example.controller;

import com.example.model.LeaveRequest;
import com.example.repository.LeaveTypeRepository;
import com.example.service.LeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leave")
@CrossOrigin(origins = "http://localhost:4200")
public class LeaveController {

    private final LeaveService leaveService;
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveController(LeaveService leaveService, LeaveTypeRepository leaveTypeRepository) {
        this.leaveService = leaveService;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    // 1️⃣ Get all leave types
    @GetMapping("/types")
    public ResponseEntity<?> getLeaveTypes() {
        return ResponseEntity.ok(leaveTypeRepository.getAllLeaveTypes());
    }

    // 2️⃣ Apply leave
    @PostMapping("/apply")
    public ResponseEntity<Map<String, String>> applyLeave(@RequestBody LeaveRequest request) {
        boolean success = leaveService.applyLeave(
                request.getUserId(),
                request.getLeaveType(),
                request.getFromDate(),
                request.getToDate(),
                request.getName(),
                request.getDescription(),
                request.getStatus()
        );

        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "Leave applied successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to apply leave");
            return ResponseEntity.status(500).body(response);
        }
    }

    // 3️⃣ Get all leave requests (original)
    @GetMapping("/all")
    public ResponseEntity<?> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveService.getAllLeaveRequests());
    }

    // 4️⃣ Get leave requests for a specific user
    @GetMapping("/my-leaves/{userId}")
    public ResponseEntity<?> getUserLeaveRequests(@PathVariable String userId) {
        List<Map<String, Object>> allLeaves = leaveService.getAllLeaveRequests();
        List<Map<String, Object>> userLeaves = new ArrayList<>();

        for (Map<String, Object> leave : allLeaves) {
            Map<String, Object> fields = (Map<String, Object>) leave.get("fields");
            if (fields != null && fields.containsKey("User (userid)")) {
                Object usersObj = fields.get("User (userid)");
                if (usersObj instanceof List) {
                    List<?> usersList = (List<?>) usersObj;
                    for (Object uid : usersList) {
                        if (userId.equals(uid.toString())) {
                            userLeaves.add(leave);
                            break;
                        }
                    }
                }
            }
        }

        return ResponseEntity.ok(userLeaves);
    }

    // 5️⃣ Get all leave requests with type name instead of type id
    @GetMapping("/all-with-type")
    public ResponseEntity<?> getAllLeaveRequestsWithType() {
        Map<String, String> leaveTypeMap = leaveTypeRepository.getAllLeaveTypes()
                .stream()
                .filter(typeMap -> typeMap.get("id") != null && typeMap.get("name") != null)
                .collect(Collectors.toMap(
                        typeMap -> typeMap.get("id").toString(),
                        typeMap -> typeMap.get("name").toString()
                ));

        List<Map<String, Object>> allLeaves = leaveService.getAllLeaveRequests();

        for (Map<String, Object> leave : allLeaves) {
            Map<String, Object> fields = (Map<String, Object>) leave.get("fields");
            if (fields != null && fields.containsKey("type id")) {
                Object typeObj = fields.get("type id");

                if (typeObj instanceof List) {
                    List<?> typeList = (List<?>) typeObj;
                    List<String> typeNames = typeList.stream()
                            .map(id -> leaveTypeMap.getOrDefault(id.toString(), "Unknown"))
                            .collect(Collectors.toList());
                    fields.put("type name", typeNames);
                } else if (typeObj != null) {
                    String typeId = typeObj.toString();
                    fields.put("type name", leaveTypeMap.getOrDefault(typeId, "Unknown"));
                }

                fields.remove("type id");
            }
        }

        return ResponseEntity.ok(allLeaves);
    }
    
    // ✅ 6️⃣ Update leave status
    // IMPORTANT: Pass the Airtable record ID (starts with "rec") OR your custom leave ID (LV001, LV002)
    @PatchMapping("/update-status/{leaveId}")
    public ResponseEntity<Map<String, String>> updateLeaveStatus(
            @PathVariable String leaveId,
            @RequestBody Map<String, String> requestBody) {

        String newStatus = requestBody.get("status");
        
        // Add logging to debug
        System.out.println("Received request to update leave: " + leaveId + " to status: " + newStatus);

        boolean success = leaveService.updateLeaveStatus(leaveId, newStatus);

        Map<String, String> response = new HashMap<>();
        if (success) {
            response.put("message", "Leave status updated successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to update leave status");
            return ResponseEntity.status(500).body(response);
        }
    }
}