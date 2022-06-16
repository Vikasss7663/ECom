package com.ecom.service;

import com.ecom.domain.CartItem;
import com.ecom.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public Flux<CartItem> getAllCartItems() {

        return cartItemRepository.findAll();
    }

    public Flux<CartItem> getCartItemsByCartId(String cartId) {

        return cartItemRepository.findByCartId(cartId);
    }

    public Mono<CartItem> getCartItemById(String cartItemId) {

        return cartItemRepository.findById(cartItemId);
    }

    public Mono<CartItem> addCartItem(CartItem cartItem) {

        String productId = cartItem.getProductId();
        String cartId = cartItem.getCartId();

        return cartItemRepository
                .findByCartIdAndProductId(cartId, productId)
                .switchIfEmpty(
                        cartItemRepository.save(cartItem)
                );
    }

    public Mono<CartItem> updateCartItem(CartItem updatedCartItem, String cartItemId) {

        return cartItemRepository.findById(cartItemId)
                .flatMap(cartItem -> {
                    cartItem.setQuantity(updatedCartItem.getQuantity());
                    cartItem.setModifiedAt(LocalDate.now());
                    return cartItemRepository.save(cartItem);
                });
    }

    public Mono<Void> deleteCartItem(String cartItemId) {

        return cartItemRepository.deleteById(cartItemId);
    }

    public Mono<Void> deleteCartItemsByCartId(String cartId) {

        return cartItemRepository.deleteByCartId(cartId);
    }
}
