package com.faang.analytics;

import com.faang.analytics.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.RetriableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class KafkaEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventProducer.class);
    private static final String[] EVENT_TYPES = {"purchase", "pageview", "cartaction", "useractivity"};
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();
    private static final AtomicLong errorCount = new AtomicLong(0);
    private static MeterRegistry meterRegistry;
    private static Timer eventTimer;

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            props.load(fis);
        }
        int eventRate = Integer.parseInt(props.getProperty("event.rate.per.second", "10000"));
        boolean metricsEnabled = Boolean.parseBoolean(props.getProperty("metrics.enabled", "true"));
        String topic = props.getProperty("topic", "ecommerce-events");

        Producer<String, String> producer = new KafkaProducer<>(props);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

        if (metricsEnabled) {
            meterRegistry = io.micrometer.core.instrument.Metrics.globalRegistry;
            eventTimer = meterRegistry.timer("event.publish.latency");
        }

        Runnable eventTask = () -> {
            for (int i = 0; i < eventRate / 10; i++) {
                String eventType = EVENT_TYPES[random.nextInt(EVENT_TYPES.length)];
                String eventJson = generateEventJson(eventType);
                long start = System.nanoTime();
                try {
                    producer.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), eventJson), (metadata, exception) -> {
                        if (exception != null) {
                            errorCount.incrementAndGet();
                            logger.error("Failed to send event: {}", exception.getMessage());
                        } else if (metricsEnabled) {
                            eventTimer.record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
                        }
                    });
                } catch (RetriableException e) {
                    logger.warn("Retriable Kafka error: {}", e.getMessage());
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                    logger.error("Producer error: {}", e.getMessage());
                }
            }
        };

        logger.info("Starting Kafka producer with event rate: {} events/sec", eventRate);
        for (int i = 0; i < 10; i++) {
            executor.scheduleAtFixedRate(eventTask, 0, 100, TimeUnit.MILLISECONDS);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down producer. Total errors: {}", errorCount.get());
            producer.close();
            executor.shutdown();
        }));
    }

    private static String generateEventJson(String eventType) {
        try {
            switch (eventType) {
                case "purchase":
                    Purchase purchase = new Purchase(
                        randomUserId(), randomProductId(), randomAmount(), random.nextInt(5) + 1, Instant.now()
                    );
                    return objectMapper.writeValueAsString(purchase);
                case "pageview":
                    PageView pageView = new PageView(
                        randomUserId(), randomPageUrl(), Instant.now()
                    );
                    return objectMapper.writeValueAsString(pageView);
                case "cartaction":
                    CartAction cartAction = new CartAction(
                        randomUserId(), randomProductId(), randomCartActionType(), random.nextInt(3) + 1, Instant.now()
                    );
                    return objectMapper.writeValueAsString(cartAction);
                case "useractivity":
                    UserActivity userActivity = new UserActivity(
                        randomUserId(), randomActivityType(), randomActivityDetails(), Instant.now()
                    );
                    return objectMapper.writeValueAsString(userActivity);
                default:
                    return "{}";
            }
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.error("Event serialization error: {}", e.getMessage());
            return "{}";
        }
    }

    // Helper methods for realistic random data
    private static String randomUserId() {
        return "user-" + (random.nextInt(10000) + 1);
    }
    private static String randomProductId() {
        return "product-" + (random.nextInt(5000) + 1);
    }
    private static double randomAmount() {
        return Math.round((random.nextDouble() * 500 + 10) * 100.0) / 100.0;
    }
    private static String randomPageUrl() {
        String[] pages = {"/home", "/search", "/product", "/cart", "/checkout"};
        return pages[random.nextInt(pages.length)];
    }
    private static String randomCartActionType() {
        String[] actions = {"add", "remove", "update"};
        return actions[random.nextInt(actions.length)];
    }
    private static String randomActivityType() {
        String[] activities = {"login", "logout", "search", "wishlist", "review"};
        return activities[random.nextInt(activities.length)];
    }
    private static String randomActivityDetails() {
        String[] details = {"success", "failed", "timeout", "new search", "add to wishlist"};
        return details[random.nextInt(details.length)];
    }
}
