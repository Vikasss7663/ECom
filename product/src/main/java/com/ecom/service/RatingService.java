package com.ecom.service;

import com.ecom.domain.Rating;
import com.ecom.repository.RatingRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class RatingService {

    private RatingRepository ratingRepository;
    private ReactiveCircuitBreaker ratingServiceCircuitBreaker;
    private MeterRegistry meterRegistry;

    public RatingService(RatingRepository ratingRepository, ReactiveCircuitBreakerFactory circuitBreakerFactory, MeterRegistry meterRegistry) {
        this.ratingRepository = ratingRepository;
        this.ratingServiceCircuitBreaker = circuitBreakerFactory.create("ratingServiceCircuitBreaker");
        this.meterRegistry = meterRegistry;
    }

    public Flux<Rating> getAllRatings(String productId) {

        Rating defaultRating1 = new Rating("1", "1", productId, 4.5d, "Awesome Product", new Date(), new Date());
        Rating defaultRating2 = new Rating("2", "2", productId, 4.2d, "Very Nice Product", new Date(), new Date());
        Rating defaultRating3 = new Rating("3", "3", productId, 5.0d, "I love this Product", new Date(), new Date());

        return ratingServiceCircuitBreaker.run(
                ratingRepository.findByProductId(productId).map(ratings -> {
                    meterRegistry.counter("rating.fetch.details", "outcome", "success").increment();
                    return ratings;
                }),
                throwable -> {
                    meterRegistry.counter("rating.fetch.details", "outcome", "failure").increment();
                    return Flux.just(defaultRating1, defaultRating2, defaultRating3);
                });
    }

    public Mono<Rating> getRatingById(String id) {

        Rating defaultRating = new Rating(id, "1", "1", 4.5d, "Awesome Product", new Date(), new Date());

        return  ratingServiceCircuitBreaker.run(ratingRepository.findById(id),
                throwable -> Mono.just(defaultRating));
    }

    public Mono<Rating> addRating(Rating rating) {

        val date = new Date();
        Rating defaultRating = new Rating("1", "1", "1", 4.5d, "Awesome Product", date, date);

        rating.setCreatedAt(date);
        rating.setModifiedAt(date);
        return ratingServiceCircuitBreaker.run(ratingRepository.save(rating),
                throwable -> Mono.just(defaultRating));
    }

    public Mono<Rating> updateRating(Rating updatedRating, String id) {

        val date = new Date();
        Rating defaultRating = new Rating(id, "1", "1", 4.5d, "Awesome Product", date, date);

        return ratingServiceCircuitBreaker.run(
                ratingRepository.findById(id)
                        .flatMap(rating -> {
                            rating.setRating(updatedRating.getRating());
                            rating.setComment(updatedRating.getComment());
                            rating.setModifiedAt(new Date());
                            return ratingRepository.save(rating);
                        }),
                throwable -> Mono.just(defaultRating)
        );
    }

    public Mono<Void> deleteRating(String id) {

        return ratingServiceCircuitBreaker.run(ratingRepository.deleteById(id),
                throwable -> Mono.empty());
    }
}