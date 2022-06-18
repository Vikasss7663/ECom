package com.ecom.controller;

import com.ecom.domain.CartItem;
import com.ecom.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@Slf4j
@CrossOrigin(origins = ORIGIN_URL)
@RestController
@RequestMapping("/v1/cart-item")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private boolean cartItemServiceLogEnabled = true;

    @GetMapping
    public Flux<CartItem> getAllCartItems(@RequestParam(value = "cart", required = false) String cartId) {

        if(cartId != null) {
            return cartItemService.getCartItemsByCartId(cartId)
                    .onErrorResume(throwable -> {
                        if(cartItemServiceLogEnabled) log.error(throwable.getMessage());
                        return Flux.empty();
                    });
        }

        return Flux.error(new Exception("cartId must be present"));
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<CartItem>> getCartItemById(@PathVariable String cartItemId) {

        return cartItemService.getCartItemById(cartItemId)
                .map(ResponseEntity.ok()::body)
                .onErrorResume(throwable -> {
                    if(cartItemServiceLogEnabled) log.error(throwable.getMessage());
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CartItem> addCartItem(@RequestBody @Valid CartItem cartItem) {

        return cartItemService.addCartItem(cartItem).log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CartItem>> updateCartItem(@RequestBody CartItem updatedCartItem,
                                                       @PathVariable String id) {

        return cartItemService.updateCartItem(updatedCartItem, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCartItem(@PathVariable String id) {

        return cartItemService.deleteCartItem(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllCartItems(@RequestParam(value = "cart", required = false) String cartId) {

        return cartItemService.deleteAllCartItems(cartId);
    }
}

