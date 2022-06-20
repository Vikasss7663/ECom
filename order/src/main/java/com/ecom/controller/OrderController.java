package com.ecom.controller;

import com.ecom.domain.OrderItem;
import com.ecom.domain.RedisOrder;
import com.ecom.redis.RedisClient;
import com.ecom.service.OrderItemService;
import com.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@Slf4j
@CrossOrigin(origins = ORIGIN_URL)
@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final RedisClient redisClient;

    @GetMapping
    public Flux<OrderItem> getAllOrderItems() {

        return orderItemService.getAllOrderItems();
    }

    @GetMapping("{id}")
    public Flux<OrderItem> getOrderItemsByOrderId(@PathVariable String id) {

        return orderItemService.getOrderItemsByOrderId(id);
    }

    @PostMapping("{id}")
    public Flux<OrderItem> addOrder(
            @PathVariable String id,
            @RequestBody List<OrderItem> orderItems
    ) {

        orderService.addOrder(id)
                .doOnNext(orderItem -> {
                    RedisOrder redisOrder = new RedisOrder(orderItem.getOrderId(), orderItem.getUserId(), new Date(), new Date());
                    redisClient.storeSerializedOrders(orderItem.getOrderId(), redisOrder, Duration.ofMinutes(5L))
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
                });

        return orderItemService.addOrderItem(orderItems);
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteOrderItems(@PathVariable String id) {

        return orderItemService.deleteAllOrderItems(id);
    }
}
