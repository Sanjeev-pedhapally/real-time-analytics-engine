package com.faang.analytics.entity;

import com.datastax.oss.driver.api.mapper.annotations.*;
import java.time.Instant;

@Entity
@CqlName("cart_actions")
public class CartActionEvent {
    @PartitionKey
    private String userId;
    @ClusteringColumn(1)
    private Instant eventTime;
    @ClusteringColumn(2)
    private String productId;
    private String actionType;
    private int quantity;
    // Getters, setters, constructors
    // ...existing code...
}
