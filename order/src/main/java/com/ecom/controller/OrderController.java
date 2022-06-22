package com.ecom.controller;

import com.ecom.domain.OrderItem;
import com.ecom.service.OrderItemService;
import com.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@Slf4j
@CrossOrigin(origins = ORIGIN_URL)
@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping
    public Flux<OrderItem> getAllOrderItems() {

        return orderItemService.getAllOrderItems();
    }

    @GetMapping("{id}")
    public Flux<OrderItem> getOrderItemsByOrderId(@PathVariable String id) {

        return orderItemService.getOrderItemsByOrderId(id);
    }

    @PostMapping("{id}")
    public Flux<OrderItem> addOrder(
            @PathVariable String id,
            @RequestBody List<OrderItem> orderItems
    ) {

        return orderService.addOrder(id)
                .flatMapMany(order -> {
                    List<OrderItem> updatedOrderItemList = orderItems
                            .parallelStream()
                            .peek(orderItem -> orderItem.setOrderId(order.getOrderId())).toList();
                    return orderItemService.addOrderItem(updatedOrderItemList);
                });
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteOrder(@PathVariable String id) {

        return orderService.deleteOrder(id);
    }
}
