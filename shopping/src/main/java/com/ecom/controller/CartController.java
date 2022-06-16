package com.ecom.controller;

import com.ecom.client.CartRestClient;
import com.ecom.domain.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartRestClient cartRestClient;

    @GetMapping
    public Mono<Cart> getCartByUserId(@RequestParam(value = "user", required = false) String userId) {

        return cartRestClient.retrieveCartById(userId);
    }

    @GetMapping("/{id}")
    public Mono<Cart> getCartById(@PathVariable String cartId) {

        return cartRestClient.retrieveCartById(cartId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cart> addCart(@RequestBody @Valid Cart cart) {

        return cartRestClient.saveCart(cart);
    }

    @PutMapping("/{id}")
    public Mono<Cart> updateCart(@RequestBody Cart updatedCart,
                                                       @PathVariable String id) {

        return cartRestClient.updateCart(updatedCart, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable String id) {

        cartRestClient.deleteCart(id);
    }
}
