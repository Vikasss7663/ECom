package com.ecom.repository;

import com.ecom.domain.Rating;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RatingRepository extends ReactiveMongoRepository<Rating, String> {

    Flux<Rating> findByProductId(String id);
}