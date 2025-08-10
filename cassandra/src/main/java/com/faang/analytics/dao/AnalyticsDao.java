package com.faang.analytics.dao;

import com.faang.analytics.entity.*;
import com.faang.analytics.config.CassandraConfig;
import com.datastax.oss.driver.api.core.CqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnalyticsDao {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsDao.class);
    private final CqlSession session;
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    public AnalyticsDao() {
        this.session = CassandraConfig.createSession();
    }

    public void insertPurchase(PurchaseEvent event) {
        executor.submit(() -> {
            try {
                session.execute("INSERT INTO purchase_events (user_id, event_time, product_id, amount, quantity) VALUES (?, ?, ?, ?, ?)",
                        event.getUserId(), event.getEventTime(), event.getProductId(), event.getAmount(), event.getQuantity());
            } catch (Exception e) {
                logger.error("Error inserting purchase event: {}", e.getMessage());
            }
        });
    }
    // Similar insert methods for PageViewEvent, CartActionEvent, UserActivityEvent
    // ...existing code...
    public void close() {
        session.close();
        executor.shutdown();
    }
}
