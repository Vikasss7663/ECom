package com.ecom.client;

import com.ecom.domain.CartItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartItemRestClient {

    private final WebClient webClient;

    @Value("${restClient.cartItemUrl}")
    private String cartItemUrl;

    public Flux<CartItem> retrieveAllCartItems(String cartId) {

        return webClient
                .get()
                .uri(cartItemUrl + "?cart=" + cartId)
                .retrieve()
                .bodyToFlux(CartItem.class);
    }

    public Mono<CartItem> retrieveCartItemById(String cartItemId) {

        return webClient
                .get()
                .uri(cartItemUrl + "/" + cartItemId)
                .retrieve()
                .bodyToMono(CartItem.class);
    }

    public Mono<CartItem> saveCartItem(CartItem cartItem) {

        return webClient
                .post()
                .uri(cartItemUrl)
                .body(Mono.just(cartItem), CartItem.class)
                .retrieve()
                .bodyToMono(CartItem.class);
    }

    public Mono<CartItem> updateCartItem(CartItem cartItem, String cartItemId) {

        return webClient
                .put()
                .uri(cartItemUrl + "/" + cartItemId)
                .body(Mono.just(cartItem), CartItem.class)
                .retrieve()
                .bodyToMono(CartItem.class);
    }

    public void deleteCartItem(String cartItemId) {

        webClient
                .delete()
                .uri(cartItemUrl + "/" + cartItemId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    public void deleteAllCartItems(String cartId) {

        webClient
                .delete()
                .uri(cartItemUrl + "?cart=" + cartId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
