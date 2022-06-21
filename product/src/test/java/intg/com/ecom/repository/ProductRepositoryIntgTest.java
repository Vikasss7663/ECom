package com.ecom.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static com.ecom.utils.DummyItems.getDummyProduct;
import static com.ecom.utils.DummyItems.getDummyProducts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
class ProductRepositoryIntgTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {

        var date = new Date();

        var listOfProducts = Arrays.asList(getDummyProducts());

        productRepository.saveAll(listOfProducts)
                .blockLast();
    }

    @AfterEach
    void tearDown() {

        productRepository.deleteAll().block();
    }

    @Test
    void findAll() {

        var productFlux = productRepository.findAll().log();

        StepVerifier.create(productFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {

        var productMono = productRepository.findById("1").log();

        StepVerifier.create(productMono)
                .assertNext(product -> {
                    assertEquals("IPHONE-13 Pro", product.getProductName());
                })
                .verifyComplete();
    }

    @Test
    void saveProduct() {

        var date = new Date();

        var product = getDummyProduct();

        var productMono = productRepository.save(product).log();

        StepVerifier.create(productMono)
                .assertNext(savedProduct -> {
                    assertEquals("Samsung - M31", savedProduct.getProductName());
                })
                .verifyComplete();
    }

    @Test
    void updateProduct() {

        var product = productRepository.findById("1").block();
        product.setProductName("IPHONE-13 MAX PRO");

        var productMono = productRepository.save(product).log();

        StepVerifier.create(productMono)
                .assertNext(updatedProduct -> {
                    assertNotNull(updatedProduct.getProductId());
                    assertEquals("IPHONE-13 MAX PRO", updatedProduct.getProductName());
                })
                .verifyComplete();
    }

    @Test
    void deleteProduct() {

        productRepository.deleteById("1").block();
        var moviesInfoFlux = productRepository.findAll().log();

        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByCategoryId() {

        var productFlux = productRepository.findByCategoryId("1").log();

        StepVerifier.create(productFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findAllByProductId() {

        HashSet<String> productIdList = new HashSet<>(Arrays.asList("1", "2"));
        var productFlux = productRepository.findAllById(productIdList).log();

        StepVerifier.create(productFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}