package com.ecom.client;

import com.ecom.domain.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartRestClient {

    private final WebClient webClient;

    @Value("${restClient.cartUrl}")
    private String cartUrl;

    public Mono<Cart> retrieveCartByUserId(String userId) {

        return webClient
                .get()
                .uri(cartUrl + "?user=" + userId)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public Mono<Cart> retrieveCartById(String cartId) {

        return webClient
                .get()
                .uri(cartUrl + "/" + cartId)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public Mono<Cart> saveCart(Cart cart) {

        return webClient
                .post()
                .uri(cartUrl)
                .body(Mono.just(cart), Cart.class)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public Mono<Cart> updateCart(Cart cart, String cartId) {

        return webClient
                .put()
                .uri(cartUrl + "/" + cartId)
                .body(Mono.just(cart), Cart.class)
                .retrieve()
                .bodyToMono(Cart.class);
    }

    public void deleteCart(String cartId) {

        webClient
                .delete()
                .uri(cartUrl + "/" + cartId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
