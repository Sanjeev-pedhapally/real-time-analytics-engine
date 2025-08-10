package com.faang.analytics.entity;

import com.datastax.oss.driver.api.mapper.annotations.*;
import java.time.Instant;

@Entity
@CqlName("pageview_events")
public class PageViewEvent {
    @PartitionKey
    private String userId;
    @ClusteringColumn(1)
    private Instant eventTime;
    @ClusteringColumn(2)
    private String pageUrl;
    // Getters, setters, constructors
    // ...existing code...
}
