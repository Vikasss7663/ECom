package com.ecom.config;

import com.ecom.properties.CategoryServiceCircuitBreakerProperties;
import com.ecom.properties.ProductServiceCircuitBreakerProperties;
import com.ecom.properties.RatingServiceCircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class Resilience4JConfig {

    private final ProductServiceCircuitBreakerProperties productServiceCircuitBreakerProperties;
    private final CategoryServiceCircuitBreakerProperties categoryServiceCircuitBreakerProperties;
    private final RatingServiceCircuitBreakerProperties ratingServiceCircuitBreakerProperties;

    @Bean("productServiceCircuitBreaker")
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> getProductServiceCircuitBreaker() {

        // the circuitBreakerConfig and timeLimiterConfig objects
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(productServiceCircuitBreakerProperties.getFailureRateThreshold())
                .waitDurationInOpenState(productServiceCircuitBreakerProperties.getWaitDurationInOpenState())
                .permittedNumberOfCallsInHalfOpenState(productServiceCircuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState())
                .minimumNumberOfCalls(productServiceCircuitBreakerProperties.getMinimumNumberOfCalls())
                .slidingWindowType(productServiceCircuitBreakerProperties.getSlidingWindowType())
                .slidingWindowSize(productServiceCircuitBreakerProperties.getSlidingWindowSize())
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build(), "productServiceCircuitBreaker");
    }

    @Bean("categoryServiceCircuitBreaker")
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> getCategoryServiceCircuitBreaker() {

        // the circuitBreakerConfig and timeLimiterConfig objects
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(categoryServiceCircuitBreakerProperties.getFailureRateThreshold())
                .waitDurationInOpenState(categoryServiceCircuitBreakerProperties.getWaitDurationInOpenState())
                .permittedNumberOfCallsInHalfOpenState(categoryServiceCircuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState())
                .minimumNumberOfCalls(categoryServiceCircuitBreakerProperties.getMinimumNumberOfCalls())
                .slidingWindowType(categoryServiceCircuitBreakerProperties.getSlidingWindowType())
                .slidingWindowSize(categoryServiceCircuitBreakerProperties.getSlidingWindowSize())
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build(), "categoryServiceCircuitBreaker");
    }

    @Bean("ratingServiceCircuitBreaker")
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> getRatingServiceCircuitBreaker() {

        // the circuitBreakerConfig and timeLimiterConfig objects
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(ratingServiceCircuitBreakerProperties.getFailureRateThreshold())
                .waitDurationInOpenState(ratingServiceCircuitBreakerProperties.getWaitDurationInOpenState())
                .permittedNumberOfCallsInHalfOpenState(ratingServiceCircuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState())
                .minimumNumberOfCalls(ratingServiceCircuitBreakerProperties.getMinimumNumberOfCalls())
                .slidingWindowType(ratingServiceCircuitBreakerProperties.getSlidingWindowType())
                .slidingWindowSize(ratingServiceCircuitBreakerProperties.getSlidingWindowSize())
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build(), "ratingServiceCircuitBreaker");
    }
}