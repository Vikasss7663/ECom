package com.ecom.controller;

import com.ecom.client.CartRestClient;
import com.ecom.domain.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartRestClient cartRestClient;

    @GetMapping
    public Mono<Cart> getCartByUserId(@RequestParam(value = "user", required = false) String userId) {

        return cartRestClient.retrieveCartById(userId);
    }
}
