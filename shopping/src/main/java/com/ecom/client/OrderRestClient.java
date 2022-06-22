package com.ecom.client;

import com.ecom.domain.CartItem;
import com.ecom.domain.OrderItem;
import com.ecom.domain.Product;
import com.ecom.domain.ProductInventory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRestClient {

    private final WebClient webClient;
    private final ProductRestClient productRestClient;

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

    public Flux<OrderItem> saveOrder(List<CartItem> cartItemList, String userId) {

        List<OrderItem> orderItemList = new ArrayList<>();
        List<ProductInventory> productInventoryList = new ArrayList<>();
        for(CartItem cartItem: cartItemList) {
            productInventoryList.add(
                    new ProductInventory(cartItem.getProductId(), cartItem.getQuantity())
            );
        }

        return productRestClient
                .decreaseProductQuantity(productInventoryList).log()
                .collectList()
                .flatMapMany(productList -> {

                    Map<String, Long> productQuantityMap = new HashMap<>();

                    for(Product product: productList)
                        productQuantityMap.put(product.getProductId(), product.getProductQuantity());

                    List<OrderItem> invalidOrderItem = new ArrayList<>();

                    for(CartItem cartItem: cartItemList) {
                        long productQuantityRemaining = productQuantityMap.getOrDefault(cartItem.getProductId(), -1L);
                        OrderItem orderItem = new OrderItem(
                                cartItem.getCartItemId(),
                                cartItem.getCartId(), // update order id later
                                cartItem.getProductId(),
                                productQuantityRemaining,
                                LocalDate.now(),
                                LocalDate.now()
                        );

                        if(productQuantityRemaining >= 0) {
                            orderItemList.add(orderItem);
                        } else {
                            invalidOrderItem.add(orderItem);
                        }
                    }

                    // if there is invalid order ( 1. product not found or 2. out of stock .)
                    if(invalidOrderItem.size() > 0) return Flux.fromStream(invalidOrderItem.parallelStream());

                    return webClient
                            .post()
                            .uri(orderUrl + "/" + userId)
                            .body(Mono.just(orderItemList), OrderItem.class)
                            .retrieve()
                            .bodyToFlux(OrderItem.class);
                });
    }

    public void deleteOrder(String orderId) {

        webClient
                .delete()
                .uri(orderUrl + "/" + orderId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
