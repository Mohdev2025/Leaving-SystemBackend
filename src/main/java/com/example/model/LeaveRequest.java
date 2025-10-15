package com.example.model;

public class LeaveRequest {
    private String leaveId;
    private String userId;
    private String leaveType;
    private String fromDate;
    private String toDate;
    private String name;
    private String description;
    private String status;

    public LeaveRequest() {}

    public LeaveRequest(String leaveId, String userId, String leaveType, String fromDate, String toDate, String name, String description, String status) {
        this.leaveId = leaveId;
        this.userId = userId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    // ✅ Getters & Setters
    public String getLeaveId() { return leaveId; }
    public void setLeaveId(String leaveId) { this.leaveId = leaveId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
