package com.ecom.controller;

import com.ecom.client.AddressRestClient;
import com.ecom.domain.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRestClient addressRestClient;

    @GetMapping
    public Flux<Address> getAllAddresses(@RequestParam(value = "user", required = false) String userId) {

        return addressRestClient.retrieveAddresses(userId);
    }

    @GetMapping("{id}")
    public Mono<Address> getAddressById(@PathVariable String addressId) {

        return addressRestClient.retrieveAddressById(addressId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Address> addAddress(@RequestBody @Valid Address address) {

        return addressRestClient.saveAddress(address);
    }

    @PutMapping("{id}")
    public Mono<Address> updateAddress(@RequestBody Address updatedAddress,
                                                       @PathVariable String id) {

        return addressRestClient.updateAddress(updatedAddress, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable String id) {

        addressRestClient.deleteAddress(id);
    }
}
