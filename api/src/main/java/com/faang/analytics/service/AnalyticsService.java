package com.faang.analytics.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.faang.analytics.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);
    private final RedisService redisService;

    public AnalyticsService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Mono<String> getMetrics() {
        return Mono.fromCallable(() -> redisService.get("metrics"))
            .map(result -> result != null ? result : "{}")
            .onErrorResume(e -> {
                logger.error("Failed to get metrics from Redis", e);
                return Mono.just("{}");
            });
    }

    public Mono<String> getPageViews() {
        return Mono.fromCallable(() -> redisService.get("pageviews"))
            .map(result -> result != null ? result : "[]")
            .onErrorResume(e -> {
                logger.error("Failed to get page views from Redis", e);
                return Mono.just("[]");
            });
    }

    public Mono<String> getCartActions() {
        return Mono.fromCallable(() -> redisService.get("cartActions"))
            .map(result -> result != null ? result : "[]")
            .onErrorResume(e -> {
                logger.error("Failed to get cart actions from Redis", e);
                return Mono.just("[]");
            });
    }
}
