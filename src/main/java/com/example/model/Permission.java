package com.example.model;

public class Permission {
    private String id;
    private String permissionType;
    private String date;
    private String from;
    private String to;
    private String reason;
    private String name ;
    private String userId;
    private String status;

    // Constructors
    public Permission() {}

    public Permission(String id, String permissionType, String date, String from, 
                     String to, String reason,String name , String userId, String status) {
        this.id = id;
        this.permissionType = permissionType;
        this.date = date;
        this.from = from;
        this.to = to;
        this.reason = reason;
        this.name= name ;
        this.userId = userId;
        this.status = status;
    }

    // Getters & Setters
    public String getId() { 
        return id; 
    }
    
    public void setId(String id) { 
        this.id = id; 
    }

    public String getPermissionType() { 
        return permissionType; 
    }
    
    public void setPermissionType(String permissionType) { 
        this.permissionType = permissionType; 
    }

    public String getDate() { 
        return date; 
    }
    
    public void setDate(String date) { 
        this.date = date; 
    }

    public String getFrom() { 
        return from; 
    }
    
    public void setFrom(String from) { 
        this.from = from; 
    }

    public String getTo() { 
        return to; 
    }
    
    public void setTo(String to) { 
        this.to = to; 
    }

    public String getReason() { 
        return reason; 
    }
    
    public void setReason(String reason) { 
        this.reason = reason; 
    }
    
    public String getname() { 
        return reason; 
    }
    
    public void setname(String reason) { 
        this.reason = reason; 
    }
    
    

    public String getUserId() { 
        return userId; 
    }
    
    public void setUserId(String userId) { 
        this.userId = userId; 
    }

    // ✅ FIXED: Correct getter and setter for status
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id='" + id + '\'' +
                ", permissionType='" + permissionType + '\'' +
                ", date='" + date + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", reason='" + reason + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}