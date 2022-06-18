package com.ecom.service;

import com.ecom.domain.Order;
import com.ecom.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Mono<Order> addOrder(String userId) {

        return orderRepository.save(new Order(null, userId, LocalDate.now(), LocalDate.now()));
    }

    public Mono<Void> deleteOrder(String orderId) {

        return orderRepository.deleteById(orderId);
    }
}