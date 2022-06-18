package com.ecom.repository;

import com.ecom.domain.OrderItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderItemRepository extends ReactiveMongoRepository<OrderItem, String> {

    Flux<OrderItem> findByOrderId(String orderId);

    Mono<OrderItem> findByOrderIdAndProductId(String orderId, String productId);

    Mono<Void> deleteByOrderId(String orderId);
}