package com.ecom.client;

import com.ecom.domain.Rating;
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
public class RatingRestClient {

    private final WebClient webClient;

    @Value("${restClient.ratingUrl}")
    private String ratingUrl;

    public Flux<Rating> retrieveRatings(String productId) {

        return webClient
                .get()
                .uri(productId != null ? (ratingUrl + "?product=" + productId) : ratingUrl)
                .retrieve()
                .bodyToFlux(Rating.class);
    }
    
    public Mono<Rating> retrieveRatingById(String ratingId) {
        
        return webClient
                .get()
                .uri(ratingUrl + "/" + ratingId)
                .retrieve()
                .bodyToMono(Rating.class);
    }

    public Mono<Rating> saveRating(Rating rating) {

        return webClient
                .post()
                .uri(ratingUrl)
                .body(Mono.just(rating), Rating.class)
                .retrieve()
                .bodyToMono(Rating.class);
    }

    public Mono<Rating> updateRating(Rating rating, String ratingId) {

        return webClient
                .put()
                .uri(ratingUrl + "/" + ratingId)
                .body(Mono.just(rating), Rating.class)
                .retrieve()
                .bodyToMono(Rating.class);
    }

    public void deleteRating(String ratingId) {

        webClient
                .delete()
                .uri(ratingUrl + "/" + ratingId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
