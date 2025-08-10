package com.faang.analytics.model;

import java.time.Instant;

public class PageView {
    private String userId;
    private String pageUrl;
    private Instant timestamp;

    public PageView(String userId, String pageUrl, Instant timestamp) {
        this.userId = userId;
        this.pageUrl = pageUrl;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
