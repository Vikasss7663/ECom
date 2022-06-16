package com.ecom.client;

import com.ecom.domain.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressRestClient {

    private final WebClient webClient;

    @Value("${restClient.addressUrl}")
    private String addressUrl;

    public Flux<Address> retrieveAddresses(String userId) {

        return webClient
                .get()
                .uri(addressUrl +"?user=" + userId)
                .retrieve()
                .bodyToFlux(Address.class);
    }

    public Mono<Address> retrieveAddressById(String addressId) {

        return webClient
                .get()
                .uri(addressUrl + "/" + addressId)
                .retrieve()
                .bodyToMono(Address.class);
    }
    

    public Mono<Address> saveAddress(Address address) {

        return webClient
                .post()
                .uri(addressUrl)
                .body(Mono.just(address), Address.class)
                .retrieve()
                .bodyToMono(Address.class);
    }

    public Mono<Address> updateAddress(Address address, String addressId) {

        return webClient
                .put()
                .uri(addressUrl + "/" + addressId)
                .body(Mono.just(address), Address.class)
                .retrieve()
                .bodyToMono(Address.class);
    }

    public void deleteAddress(String addressId) {

        webClient
                .delete()
                .uri(addressUrl + "/" + addressId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
