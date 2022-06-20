package com.ecom.controller;

import com.ecom.domain.Category;
import com.ecom.repository.CategoryRepository;
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
class CategoryControllerIntgTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    WebTestClient webTestClient;

    static String CATEGORY_URL = "/v1/category";

    @BeforeEach
    void setUp() {

        var date = new Date();

        var listOfCategories = List.of(
                new Category("1", "MOBILE", "Mobile Technology" , date, date),
                new Category("2", "LAPTOP", "Laptop Technology" , date, date)
        );

        categoryRepository.saveAll(listOfCategories)
                .blockLast();
    }

    @AfterEach
    void tearDown() {

        categoryRepository.deleteAll().block();
    }

    @Test
    void getAllCategories() {

        webTestClient
                .get()
                .uri(CATEGORY_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById() {

        var id = "1";
        webTestClient
                .get()
                .uri(CATEGORY_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Category.class)
                .consumeWith(categoryEntityExchangeResult -> {

                    var category = categoryEntityExchangeResult.getResponseBody();

                    assertNotNull(category);
                    assertEquals("1", category.getCategoryId());
                    assertEquals("MOBILE", category.getCategoryName());
                });
    }

    @Test
    void getCategoryByIdNotFound() {

        var id = "3";
        webTestClient
                .get()
                .uri(CATEGORY_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addCategory() {

        var date = new Date();

        var category = new Category("3", "CAR", "Car Collection" , date, date);


        webTestClient
        .post()
        .uri(CATEGORY_URL)
        .bodyValue(category)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Category.class)
        .consumeWith(categoryEntityExchangeResult -> {

            var saveCategory = categoryEntityExchangeResult.getResponseBody();

            assertNotNull(saveCategory);
            assertNotNull(saveCategory.getCategoryId());
        });
    }

    @Test
    void updateCategory() {

        var date = new Date();

        var id = "1";
        var category = new Category("2", "LAPTOP", "Updated - Laptop Technology" , date, date);

        webTestClient
                .put()
                .uri(CATEGORY_URL+"{id}", id)
                .bodyValue(category)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Category.class)
                .consumeWith(categoryEntityExchangeResult -> {

                    var updatedCategory = categoryEntityExchangeResult.getResponseBody();

                    assertNotNull(updatedCategory);
                    assertNotNull(updatedCategory.getCategoryId());
                    assertEquals("Updated - Laptop Technology", updatedCategory.getCategoryDesc());
                });
    }

    @Test
    void deleteCategory() {

        var id = "1";

        webTestClient
                .delete()
                .uri(CATEGORY_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

}