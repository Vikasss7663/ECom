package com.ecom.client;

import com.ecom.domain.CartItem;
import com.ecom.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRestClient {

    private final WebClient webClient;

    @Value("${restClient.orderUrl}")
    private String orderUrl;

    public Flux<OrderItem> retrieveAllOrderItems() {

        return  webClient
                .get()
                .uri(orderUrl)
                .retrieve()
                .bodyToFlux(OrderItem.class);
    }

    public Flux<OrderItem> retrieveOrderItemsByOrderId(String orderId)  {

        return  webClient
                .get()
                .uri(orderUrl + "/" + orderId)
                .retrieve()
                .bodyToFlux(OrderItem.class);
    }

    public Flux<OrderItem> saveOrder(String userId, List<CartItem> cartItemList) {

        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartItem cartItem: cartItemList) {
            orderItemList.add(
                    new OrderItem(
                            cartItem.getCartItemId(),
                            cartItem.getCartId(),
                            cartItem.getProductId(),
                            cartItem.getQuantity(),
                            LocalDate.now(),
                            LocalDate.now())
            );
        }

        return webClient
                .post()
                .uri(orderUrl + "/" + userId)
                .body(Mono.just(orderItemList), OrderItem.class)
                .retrieve()
                .bodyToFlux(OrderItem.class);
    }

    public void deleteOrderItems(String orderId) {

        webClient
                .delete()
                .uri(orderUrl + "/" + orderId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
