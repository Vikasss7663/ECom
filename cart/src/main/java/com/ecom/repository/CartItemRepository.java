package com.ecom.repository;

import com.ecom.domain.CartItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartItemRepository extends ReactiveMongoRepository<CartItem, String> {

    Flux<CartItem> findByCartId(String cartId);

    Mono<CartItem> findByCartIdAndProductId(String cartId, String productId);

    Mono<Void> deleteByCartId(String cartId);
}

