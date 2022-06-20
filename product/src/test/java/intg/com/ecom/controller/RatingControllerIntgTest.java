package com.ecom.controller;

import com.ecom.domain.Rating;
import com.ecom.repository.RatingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class RatingControllerIntgTest {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    WebTestClient webTestClient;

    static String RATING_URL = "/v1/rating";

    @BeforeEach
    void setUp() {

        var date = new Date();

        var listOfCategories = List.of(
                new Rating("1", "1", "1", 5.0, "Awesome Product, finally my dream came true", date, date),
                new Rating("2", "2", "1", 4.5, "This is wonderful product, value for money", date, date)
        );

        ratingRepository.saveAll(listOfCategories)
                .blockLast();
    }

    @AfterEach
    void tearDown() {

        ratingRepository.deleteAll().block();
    }

    @Test
    void getAllRatings() {

        var productId = "1";

        var uri = UriComponentsBuilder.fromUriString(RATING_URL)
                .queryParam("product", productId)
                .buildAndExpand().toUri();

        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Rating.class)
                .hasSize(2);
    }

    @Test
    void getRatingById() {

        var id = "1";
        webTestClient
                .get()
                .uri(RATING_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Rating.class)
                .consumeWith(ratingEntityExchangeResult -> {

                    var rating = ratingEntityExchangeResult.getResponseBody();

                    assertNotNull(rating);
                    assertEquals("1", rating.getRatingId());
                    assertEquals(5.0, rating.getRating());
                });
    }

    @Test
    void getRatingByIdNotFound() {

        var id = "3";
        webTestClient
                .get()
                .uri(RATING_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addRating() {

        var date = new Date();

        var rating = new Rating("3", "3", "1", 5.0, "Finally, I had bought my kidney, just to write this comment." , date, date);

        webTestClient
        .post()
        .uri(RATING_URL)
        .bodyValue(rating)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Rating.class)
        .consumeWith(ratingEntityExchangeResult -> {

            var saveRating = ratingEntityExchangeResult.getResponseBody();

            assertNotNull(saveRating);
            assertNotNull(saveRating.getRatingId());
        });
    }

    @Test
    void updateRating() {

        var date = new Date();

        var id = "1";
        var rating = new Rating("1", "2", "1", 4.5, "It's always good to use it." , date, date);

        webTestClient
                .put()
                .uri(RATING_URL+"{id}", id)
                .bodyValue(rating)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Rating.class)
                .consumeWith(ratingEntityExchangeResult -> {

                    var updatedRating = ratingEntityExchangeResult.getResponseBody();

                    assertNotNull(updatedRating);
                    assertNotNull(updatedRating.getRatingId());
                    assertEquals(4.5, updatedRating.getRating());
                });
    }

    @Test
    void deleteRating() {

        var id = "1";

        webTestClient
                .delete()
                .uri(RATING_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

}