package com.faang.analytics.model;

import java.time.Instant;

public class UserActivity {
    private String userId;
    private String activityType; // login, logout, search, etc.
    private String details;
    private Instant timestamp;

    // Constructors, getters, setters
    public UserActivity(String userId, String activityType, String details, Instant timestamp) {
        this.userId = userId;
        this.activityType = activityType;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getDetails() {
        return details;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
