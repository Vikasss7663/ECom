package com.ecom.repository;

import com.ecom.domain.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
class AddressRepositoryIntgTest {

    @Autowired
    AddressRepository addressRepository;

    @BeforeEach
    void setUp() {

        var date = new Date();

        var listOfAddresses = List.of(
                new Address("1", "1", "Devgaon(nua)", "Near Power House", "333707", "jhunjhunu", "rajasthan", "india", date, date),
                new Address("2", "2", "Sabalpura", "Main Road", "331021", "sikar", "rajasthan", "india", date, date)
        );

        addressRepository.saveAll(listOfAddresses)
                .blockLast();
    }

    @AfterEach
    void tearDown() {

        addressRepository.deleteAll().block();
    }

    @Test
    void findAll() {

        var addressFlux = addressRepository.findAll().log();

        StepVerifier.create(addressFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {

        var addressMono = addressRepository.findById("1").log();

        StepVerifier.create(addressMono)
                .assertNext(address -> {
                    assertEquals("333707", address.getPostalCode());
                })
                .verifyComplete();
    }

    @Test
    void saveAddress() {

        var date = new Date();

        var address = new Address("3", "1", "Malaviya Nagar", "Jhalana Gram", "302017", "jaipur", "rajasthan", "india", date, date);

        var addressMono = addressRepository.save(address).log();

        StepVerifier.create(addressMono)
                .assertNext(savedAddress -> {
                    assertEquals("302017", savedAddress.getPostalCode());
                })
                .verifyComplete();
    }

    @Test
    void findByUserId() {
    }
}