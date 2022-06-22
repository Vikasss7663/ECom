package com.ecom.service;

import com.ecom.domain.Order;
import com.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public Mono<Order> addOrder(String userId) {

        return orderRepository.save(new Order(null, userId, LocalDate.now(), LocalDate.now()));
    }

    public Mono<Void> deleteOrder(String orderId) {

        return orderRepository.deleteById(orderId)
                .switchIfEmpty(orderItemService.deleteAllOrderItems(orderId))
                .subscribeOn(Schedulers.boundedElastic());
    }
}