package com.ecom.repository;

import com.ecom.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    public Flux<User> findByUserEmailAndUserPassword(String userName, String userPassword);
}

