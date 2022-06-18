package com.ecom.service;

import com.ecom.domain.OrderItem;
import com.ecom.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    /*
     * Scheduler is using this to find all order items to delete
     * If they expires
     */
    public Flux<OrderItem> getAllOrderItems() {

        return orderItemRepository.findAll();
    }

    public Flux<OrderItem> getOrderItemsByOrderId(String orderId) {

        return orderItemRepository.findByOrderId(orderId);
    }

    public Mono<OrderItem> getOrderItemById(String orderItemId) {

        return orderItemRepository.findById(orderItemId);
    }

    public Mono<OrderItem> addOrderItem(OrderItem orderItem) {

        return orderItemRepository.save(orderItem);
    }

    public Flux<OrderItem> addOrderItem(List<OrderItem> orderItemList) {

        return orderItemRepository.saveAll(orderItemList);
    }

    public Mono<OrderItem> updateOrderItem(OrderItem updatedOrderItem, String orderItemId) {

        return orderItemRepository.findById(orderItemId)
                .flatMap(orderItem -> {
                    orderItem.setQuantity(updatedOrderItem.getQuantity());
                    orderItem.setModifiedAt(LocalDate.now());
                    return orderItemRepository.save(orderItem);
                });
    }

    public Mono<Void> deleteOrderItem(String orderItemId) {

        return orderItemRepository.deleteById(orderItemId);
    }

    public Mono<Void> deleteAllOrderItems(String orderId) {

        return orderItemRepository.deleteByOrderId(orderId);
    }
}
