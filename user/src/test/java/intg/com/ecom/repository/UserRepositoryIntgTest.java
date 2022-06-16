package com.ecom.repository;

import com.ecom.domain.User;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
class UserRepositoryIntgTest {


    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        var date = new Date();

        var listOfUsers = List.of(
                new User("1", "Vishal Singh", "vishalsinghgk2018@gmail.com", "123456", "1234567890", date, date),
                new User("2", "Satish Singh", "satishss9502@gmail.com", "123456", "1234567890", date, date)
        );

        userRepository.saveAll(listOfUsers)
                .blockLast();
    }

    @AfterEach
    void tearDown() {

        userRepository.deleteAll().block();
    }

    @Test
    void findByUserEmailAndUserPassword() {

        var email = "vishalsinghgk2018@gmail.com";
        var password = "123456";
        var userMono = userRepository.findByUserEmailAndUserPassword(email, password);

        StepVerifier.create(userMono)
                .assertNext(user -> {
                    assertEquals("1", user.getUserId());
                })
                .verifyComplete();
    }
}