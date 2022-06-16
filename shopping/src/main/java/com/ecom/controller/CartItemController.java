package com.ecom.controller;

import com.ecom.client.CartItemRestClient;
import com.ecom.domain.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cart-item")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemRestClient cartItemRestClient;

    @GetMapping
    public Flux<CartItem> getAllCartItems(@RequestParam(value = "cart", required = false) String cartId) {

        return cartItemRestClient.retrieveAllCartItems(cartId);
    }

    @GetMapping("/{id}")
    public Mono<CartItem> getCartItemById(@PathVariable String cartItemId) {

        return cartItemRestClient.retrieveCartItemById(cartItemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CartItem> addCartItem(@RequestBody @Valid CartItem cartItem) {

        return cartItemRestClient.saveCartItem(cartItem);
    }

    @PutMapping("/{id}")
    public Mono<CartItem> updateCartItem(@RequestBody CartItem updatedCartItem,
                                                       @PathVariable String id) {

        return cartItemRestClient.updateCartItem(updatedCartItem, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable String id) {

        cartItemRestClient.deleteCartItem(id);
    }
}
