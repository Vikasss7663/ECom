package com.ecom.controller;

import com.ecom.client.RatingRestClient;
import com.ecom.domain.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingRestClient ratingRestClient;

    @GetMapping
    public Flux<Rating> getAllRatings(@RequestParam(value = "product", required = false) String productId) {

        return ratingRestClient.retrieveRatings(productId);
    }

    @GetMapping("{id}")
    public Mono<Rating> getRatingById(@PathVariable String ratingId) {

        return ratingRestClient.retrieveRatingById(ratingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Rating> addRating(@RequestBody @Valid Rating rating) {

        return ratingRestClient.saveRating(rating);
    }

    @PutMapping("{id}")
    public Mono<Rating> updateRating(@RequestBody Rating updatedRating,
                                                       @PathVariable String id) {

        return ratingRestClient.updateRating(updatedRating, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(@PathVariable String id) {

        ratingRestClient.deleteRating(id);
    }
}
