package com.ecom.repository;

import com.ecom.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.List;

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
}