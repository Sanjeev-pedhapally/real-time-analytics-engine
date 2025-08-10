package com.faang.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@SpringBootApplication
@EnableWebFlux
public class ApiApplication implements WebFluxConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
    // Add custom WebFlux config beans if needed
}
