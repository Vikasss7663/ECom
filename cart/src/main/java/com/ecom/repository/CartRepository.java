package com.ecom.repository;

import com.ecom.domain.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CartRepository extends ReactiveMongoRepository<Cart, String> {

    Mono<Cart> findByUserId(String userId);
}
