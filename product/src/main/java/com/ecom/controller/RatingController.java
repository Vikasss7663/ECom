package com.ecom.controller;

import com.ecom.domain.Rating;
import com.ecom.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/rating")
public class RatingController {

    private RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public Flux<Rating> getAllRatings(@RequestParam(value = "product", required = false) String productId) {

        if(productId == null) return null;

        return ratingService.getAllRatings(productId).log();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Rating>> getRatingById(@PathVariable String id) {

        return ratingService.getRatingById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Rating> addRating(@RequestBody @Valid Rating rating) {

        return ratingService.addRating(rating).log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Rating>> updateRating(@RequestBody Rating updatedRating,
                                                       @PathVariable String id) {

        return ratingService.updateRating(updatedRating, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRating(@PathVariable String id) {

        return ratingService.deleteRating(id);
    }
}
