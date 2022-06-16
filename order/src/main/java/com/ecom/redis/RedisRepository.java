package com.ecom.redis;

import com.ecom.domain.RedisOrder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class RedisRepository {
    private final ReactiveRedisOperations<String, RedisOrder> reactiveRedisOperations;

    public RedisRepository(@Qualifier("reactiveRedisOperations") ReactiveRedisOperations<String, RedisOrder> reactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
    }

    public Mono<Boolean> save(String key, RedisOrder serializedOffers, Duration ttl) {
        return reactiveRedisOperations.opsForValue().set(key, serializedOffers, ttl);
    }

    public Mono<RedisOrder> findByKey(String key) {
        return reactiveRedisOperations.opsForValue().get(key);
    }

    public Mono<Duration> getTtl(String key) {
        return reactiveRedisOperations.getExpire(key);
    }
}