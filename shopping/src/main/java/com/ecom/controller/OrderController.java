package com.ecom.controller;

import com.ecom.client.CartItemRestClient;
import com.ecom.client.CartRestClient;
import com.ecom.client.OrderRestClient;
import com.ecom.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@Slf4j
@CrossOrigin(origins = ORIGIN_URL)
@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRestClient orderRestClient;
    private final CartItemRestClient cartItemRestClient;
    private final CartRestClient cartRestClient;


    @GetMapping
    public Flux<OrderItem> getAllOrderItems() {

        return orderRestClient.retrieveAllOrderItems();
    }

    @GetMapping("{id}")
    public Flux<OrderItem> getOrderItemsByOrderId(@PathVariable String id) {

        return orderRestClient.retrieveOrderItemsByOrderId(id);
    }

    @PostMapping("{id}")
    public Flux<OrderItem> addOrder(
            @PathVariable String id
    ) {

       return cartRestClient.retrieveCartByUserId(id)
               .flatMapMany(cart ->
                       cartItemRestClient
                               .retrieveAllCartItems(cart.getCartId())
                               .collectList()
                               .flatMapMany(cartItemList -> orderRestClient.saveOrder(cartItemList, cart.getUserId()))
               );
    }

    @DeleteMapping("{id}")
    public void deleteOrder(@PathVariable String id) {

        orderRestClient.deleteOrder(id);
    }
}
