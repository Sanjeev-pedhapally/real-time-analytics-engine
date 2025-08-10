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
            .onErrorResume(e -> {
                logger.error("Error fetching metrics: {}", e.getMessage());
                return Mono.just("{}");
            });
    }

    public Mono<String> getTopProducts() {
        return Mono.fromCallable(() -> redisService.get("top-products"))
            .onErrorResume(e -> {
                logger.error("Error fetching top products: {}", e.getMessage());
                return Mono.just("[]");
            });
    }

    public Mono<String> getRevenue() {
        return Mono.fromCallable(() -> redisService.get("revenue"))
            .onErrorResume(e -> {
                logger.error("Error fetching revenue: {}", e.getMessage());
                return Mono.just("0");
            });
    }
}
