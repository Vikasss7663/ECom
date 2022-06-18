package com.ecom.controller;

import com.ecom.domain.Order;
import com.ecom.domain.OrderItem;
import com.ecom.service.OrderItemService;
import com.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@CrossOrigin(origins = ORIGIN_URL)
@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping("{id}")
    public Mono<Order> addOrder(
            @PathVariable String id,
            @RequestBody List<OrderItem> orderItems
    ) {

        Mono<Order> order = orderService.addOrder(id);
        orderItemService.addOrderItem(orderItems);
        return order;
    }
}
