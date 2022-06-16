package com.ecom.client;

import com.ecom.domain.Category;
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
public class CategoryRestClient {

    private final WebClient webClient;

    @Value("${restClient.categoryUrl}")
    private String categoryUrl;

    public Flux<Category> retrieveCategories() {

        return webClient
                .get()
                .uri(categoryUrl)
                .retrieve()
                .bodyToFlux(Category.class);
    }

    public Mono<Category> retrieveCategoryById(String categoryId) {

        return webClient
                .get()
                .uri(categoryUrl + "/" + categoryId)
                .retrieve()
                .bodyToMono(Category.class);
    }

    public Mono<Category> saveCategory(Category category) {

        return webClient
                .post()
                .uri(categoryUrl)
                .body(Mono.just(category), Category.class)
                .retrieve()
                .bodyToMono(Category.class);
    }

    public Mono<Category> updateCategory(Category category, String categoryId) {

        return webClient
                .put()
                .uri(categoryUrl + "/" + categoryId)
                .body(Mono.just(category), Category.class)
                .retrieve()
                .bodyToMono(Category.class);
    }

    public void deleteCategory(String categoryId) {

        webClient
                .delete()
                .uri(categoryUrl + "/" + categoryId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
