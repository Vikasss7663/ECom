package com.ecom.controller;

import com.ecom.domain.RedisOrder;
import com.ecom.redis.RedisClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisClient redisClient;

    @GetMapping("/set/{orderId}")
    public Disposable set(@PathVariable String orderId) {
        RedisOrder order = new RedisOrder(orderId, orderId, new Date(), new Date());
        return redisClient.storeSerializedOrders(orderId, order, Duration.ofMinutes(5L))
                .doOnNext(status -> {
                    if (Boolean.TRUE.equals(status)) {
                        log.info("Data stored in redis.");
                    } else {
                        log.warn("Unable to store data in redis.");
                    }
                })
                .doOnError(throwable -> log.error("Unable to store data in redis. Error: [{}]", throwable.getMessage()))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    @GetMapping("/get/{orderId}")
    public Mono<Optional<RedisOrder>> get(@PathVariable String orderId) {
        return redisClient.fetchSerializedOrders(orderId)
                .doOnNext(orderDataOp -> {
                    if (!orderDataOp.isPresent()) {
                        log.warn("Redis orders could not be retrieved.");
                    } else {
                        log.info("Fetched redis orders successfully.");
                        log.debug("Redis orders: [{}]", orderDataOp);
                    }
                })
                .doOnError(throwable -> log.warn("Error in fetching orders"));
    }


}
