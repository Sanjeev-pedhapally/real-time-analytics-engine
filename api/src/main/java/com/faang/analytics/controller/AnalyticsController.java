package com.faang.analytics.controller;

import com.faang.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/metrics")
    public Mono<ResponseEntity<String>> getMetrics() {
        return analyticsService.getMetrics()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(e.getMessage())));
    }

    @GetMapping("/top-products")
    public Mono<ResponseEntity<String>> getTopProducts() {
        return analyticsService.getTopProducts()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(e.getMessage())));
    }

    @GetMapping("/revenue")
    public Mono<ResponseEntity<String>> getRevenue() {
        return analyticsService.getRevenue()
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body(e.getMessage())));
    }
}
