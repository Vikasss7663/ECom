package com.ecom.scheduler;

import com.ecom.domain.CartItem;
import com.ecom.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class CartItemExpiryScheduler {

    private final long fixedDelay = 60* 1000L;
    private final int expiryDays = 0;

    private final CartItemService cartItemService;

    @Scheduled(fixedDelay = fixedDelay)
    public void deleteCartItemScheduler() {

        Flux<CartItem> cartItemFlux = cartItemService.getAllCartItems();

        cartItemFlux.doOnNext(cartItem -> {
            LocalDate modifiedDate = cartItem.getModifiedAt();
            LocalDate nowDate = LocalDate.now();

            long durationInDays = ChronoUnit.DAYS.between(modifiedDate, nowDate);

            if(durationInDays >= expiryDays) {
                // delete this item
                cartItemService.deleteCartItem(cartItem.getCartItemId())
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe();
            }
        }).subscribe();
    }
}