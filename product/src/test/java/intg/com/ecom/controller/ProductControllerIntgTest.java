package com.ecom.controller;

import com.ecom.domain.Product;
import com.ecom.repository.ProductRepository;
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

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ProductControllerIntgTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WebTestClient webTestClient;

    static String PRODUCT_URL = "/v1/product";

    @BeforeEach
    void setUp() {

        var date = LocalDate.now();

        var listOfProducts = List.of(
                new Product("1", "IPHONE-13 Pro", "", 120000d, "", 10, "1" , date, date),
                new Product("2", "Macbook Pro - 13 Inch", "", 100000d, "", 111 , "2", date, date)
        );

        productRepository.saveAll(listOfProducts)
                .blockLast();
    }

    @AfterEach
    void tearDown() {

        productRepository.deleteAll().block();
    }

    @Test
    void getAllProducts() {

        webTestClient
                .get()
                .uri(PRODUCT_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Product.class)
                .hasSize(2);
    }

    @Test
    void getProductById() {

        var id = "1";
        webTestClient
                .get()
                .uri(PRODUCT_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Product.class)
                .consumeWith(productEntityExchangeResult -> {

                    var product = productEntityExchangeResult.getResponseBody();

                    assertNotNull(product);
                    assertEquals("1", product.getProductId());
                    assertEquals("IPHONE-13 Pro", product.getProductName());
                });
    }

    @Test
    void getProductByIdNotFound() {

        var id = "3";
        webTestClient
                .get()
                .uri(PRODUCT_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void addProduct() {

        var date = LocalDate.now();

        var product = new Product("3", "Samsung - M31", "", 18000d, "", 10, "1" , date, date);

        webTestClient
        .post()
        .uri(PRODUCT_URL)
        .bodyValue(product)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Product.class)
        .consumeWith(productEntityExchangeResult -> {

            var saveProduct = productEntityExchangeResult.getResponseBody();

            assertNotNull(saveProduct);
            assertNotNull(saveProduct.getProductId());
        });
    }

    @Test
    void addProductValidation() {

        var date = LocalDate.now();

        var product = new Product("3", null, " " , -18000d, " ", 100, "1" , date, date);

        webTestClient
                .post()
                .uri(PRODUCT_URL)
                .bodyValue(product)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    System.out.println(responseBody);
                    var expectedErrorMessage = "product.name must be present,product.price must be positive";
                    assertEquals(expectedErrorMessage, responseBody);
                    assert responseBody!=null;
                });
    }

    @Test
    void updateProduct() {

        var date = LocalDate.now();

        var id = "1";
        var product = new Product("1", "IPHONE-13 Pro", "", 130000d, "", 10, "1" , date, date);

        webTestClient
                .put()
                .uri(PRODUCT_URL+"{id}", id)
                .bodyValue(product)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Product.class)
                .consumeWith(productEntityExchangeResult -> {

                    var updatedProduct = productEntityExchangeResult.getResponseBody();

                    assertNotNull(updatedProduct);
                    assertNotNull(updatedProduct.getProductId());
                    assertEquals(130000d, updatedProduct.getProductPrice());
                });
    }

    @Test
    void deleteProduct() {

        var id = "1";

        webTestClient
                .delete()
                .uri(PRODUCT_URL+"{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void getProductByCategory() {

        var categoryId = "1";

        var uri = UriComponentsBuilder.fromUriString(PRODUCT_URL)
                .queryParam("category", categoryId)
                .buildAndExpand().toUri();

        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Product.class)
                .hasSize(1);
    }
}