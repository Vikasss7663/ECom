package com.ecom.client;

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

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRestClient {

    private final WebClient webClient;

    @Value("${restClient.productUrl}")
    private String productUrl;

    public Flux<Product> retrieveProducts(String categoryId) {

        return webClient
                .get()
                .uri(categoryId != null ? (productUrl + "?category=" + categoryId) : productUrl)
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Mono<Product> retrieveProductById(String productId) {

        return webClient
                .get()
                .uri(productUrl + "/" + productId)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Product> saveProduct(Product product) {

        return webClient
                .post()
                .uri(productUrl)
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public Mono<Product> updateProduct(Product product, String productId) {

        return webClient
                .put()
                .uri(productUrl + "/" + productId)
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class);
    }

    public void deleteProduct(String productId) {

        webClient
                .delete()
                .uri(productUrl + "/" + productId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    public Flux<Product> decreaseProductQuantity(List<ProductInventory> productInventoryList) {

        return webClient
                .put()
                .uri(productUrl + "/dec")
                .body(Mono.just(productInventoryList), ProductInventory.class)
                .retrieve()
                .bodyToFlux(Product.class);
    }

    public Flux<Product> increaseProductQuantity(List<ProductInventory> productInventoryList) {

        return webClient
                .put()
                .uri(productUrl + "/inc")
                .body(Mono.just(productInventoryList), ProductInventory.class)
                .retrieve()
                .bodyToFlux(Product.class);
    }
}