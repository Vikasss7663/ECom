package com.ecom.repository;

import com.ecom.domain.Address;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AddressRepository extends ReactiveMongoRepository<Address, String> {

    Flux<Address> findByUserId(String userId);
}
