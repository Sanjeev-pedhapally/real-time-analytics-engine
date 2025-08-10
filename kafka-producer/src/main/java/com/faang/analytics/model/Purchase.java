package com.faang.analytics.model;

import java.time.Instant;

public class Purchase {
    private String userId;
    private String productId;
    private double amount;
    private int quantity;
    private Instant timestamp;

    public Purchase(String userId, String productId, double amount, int quantity, Instant timestamp) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public double getAmount() {
        return amount;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
