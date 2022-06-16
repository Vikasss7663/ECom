package com.ecom.service;

import com.ecom.domain.Cart;
import com.ecom.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Mono<Cart> getCartByUserId(String userId) {

        return cartRepository.findByUserId(userId)
                .switchIfEmpty(
                        addCart(userId)
                );
    }

    private Mono<Cart> addCart(String userId) {

        return cartRepository.save(new Cart(null, userId, LocalDate.now(), LocalDate.now()));
    }

    public Mono<Void> deleteCart(String cartId) {

        return cartRepository.deleteById(cartId);
    }
}
