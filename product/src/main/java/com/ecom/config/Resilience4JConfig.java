package com.ecom.config;

import com.ecom.properties.ProductManagementServiceCircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class Resilience4JConfig {

    private final ProductManagementServiceCircuitBreakerProperties productManagementServiceCircuitBreakerProperties;

    @Bean("circuitBreakerFactory")
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> getCircuitBreakerFactory() {

        // the circuitBreakerConfig and timeLimiterConfig objects
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(productManagementServiceCircuitBreakerProperties.getFailureRateThreshold())
                .waitDurationInOpenState(productManagementServiceCircuitBreakerProperties.getWaitDurationInOpenState())
                .permittedNumberOfCallsInHalfOpenState(productManagementServiceCircuitBreakerProperties.getPermittedNumberOfCallsInHalfOpenState())
                .minimumNumberOfCalls(productManagementServiceCircuitBreakerProperties.getMinimumNumberOfCalls())
                .slidingWindowType(productManagementServiceCircuitBreakerProperties.getSlidingWindowType())
                .slidingWindowSize(productManagementServiceCircuitBreakerProperties.getSlidingWindowSize())
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }

}