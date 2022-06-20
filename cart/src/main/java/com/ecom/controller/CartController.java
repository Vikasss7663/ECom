package com.ecom.controller;

import com.ecom.domain.Cart;
import com.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@CrossOrigin(origins = ORIGIN_URL)
@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Mono<Cart> getCartByUserId(@RequestParam(value = "user", required = false) String userId) {

        if(userId != null) {
            return cartService.getCartByUserId(userId);
        }

        return Mono.empty();
    }

    @GetMapping("{id}")
    public Mono<Cart> getCartById(@PathVariable String id) {

        return cartService.getCartById(id);
    }
}