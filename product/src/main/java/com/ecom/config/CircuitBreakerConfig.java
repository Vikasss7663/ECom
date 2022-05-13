package com.ecom.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@RequiredArgsConstructor
public class CircuitBreakerConfig {

    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    @Bean("productServiceCircuitBreaker")
    public ReactiveCircuitBreaker getProductServiceCircuitBreaker() {

        return circuitBreakerFactory.create("productServiceCircuitBreaker");
    }

    @Bean("categoryServiceCircuitBreaker")
    public ReactiveCircuitBreaker getCategoryServiceCircuitBreaker() {

        return circuitBreakerFactory.create("categoryServiceCircuitBreaker");
    }

    @Bean("ratingServiceCircuitBreaker")
    public ReactiveCircuitBreaker getRatingServiceCircuitBreaker() {

        return circuitBreakerFactory.create("ratingServiceCircuitBreaker");
    }
}