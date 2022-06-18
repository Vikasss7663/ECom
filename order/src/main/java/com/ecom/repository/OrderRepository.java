package com.ecom.repository;

import com.ecom.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {

    Mono<Order> findByUserId(String userId);
}
