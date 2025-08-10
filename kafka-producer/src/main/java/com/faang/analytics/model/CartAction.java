package com.faang.analytics.model;

import java.time.Instant;

public class CartAction {
    private String userId;
    private String productId;
    private String actionType; // add, remove, update
    private int quantity;
    private Instant timestamp;

    // Constructors, getters, setters
    public CartAction(String userId, String productId, String actionType, int quantity, Instant timestamp) {
        this.userId = userId;
        this.productId = productId;
        this.actionType = actionType;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public String getActionType() {
        return actionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
