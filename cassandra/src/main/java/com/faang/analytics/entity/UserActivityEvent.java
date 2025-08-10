package com.faang.analytics.entity;

import com.datastax.oss.driver.api.mapper.annotations.*;
import java.time.Instant;

@Entity
@CqlName("user_activities")
public class UserActivityEvent {
    @PartitionKey
    private String userId;
    @ClusteringColumn(1)
    private Instant eventTime;
    @ClusteringColumn(2)
    private String activityType;
    private String details;
    // Getters, setters, constructors
    // ...existing code...
}
