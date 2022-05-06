package com.ecom.service;

import com.ecom.domain.Product;
import com.ecom.domain.Rating;
import com.ecom.repository.RatingRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class RatingService {

    private RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Flux<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Mono<Rating> getRatingById(String id) {
        return  ratingRepository.findById(id);
    }

    public Mono<Rating> addRating(Rating rating) {

        val date = new Date();
        rating.setCreatedAt(date);
        rating.setModifiedAt(date);
        return ratingRepository.save(rating);
    }

    public Mono<Rating> updateRating(Rating updatedRating, String id) {

        return ratingRepository.findById(id)
                .flatMap(rating -> {
                    rating.setRating(updatedRating.getRating());
                    rating.setComment(updatedRating.getComment());
                    rating.setModifiedAt(new Date());
                    return ratingRepository.save(rating);
                });
    }

    public Mono<Void> deleteRating(String id) {

        return ratingRepository.deleteById(id);
    }

    public Flux<Rating> getAllRatings(String productId) {

        return ratingRepository.findByProductId(productId);
    }

}