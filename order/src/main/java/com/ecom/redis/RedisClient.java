package com.ecom.redis;

import com.ecom.domain.RedisOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisClient {

    private final RedisRepository redisRepository;

    public Mono<Optional<RedisOrder>> fetchSerializedOrders(String orderId) {
        return redisRepository.findByKey(orderId)
                .map(Optional::ofNullable)
                .defaultIfEmpty(Optional.empty());
    }

    public Mono<Boolean> storeSerializedOrders(String orderId, RedisOrder serializedOrders, Duration ttl) {
        return redisRepository.save(orderId, serializedOrders, ttl);
    }

    Mono<Duration> getTtl(String orderId) {
        return redisRepository.getTtl(orderId);
    }
}