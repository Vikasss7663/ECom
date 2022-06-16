package com.ecom.controller;

import com.ecom.domain.Address;
import com.ecom.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/address")
public class AddressController {

    private AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public Flux<Address> getAllAddresses(@RequestParam(value = "user", required = false) String userId) {

        if(userId == null) return Flux.empty(); // throw error

        return addressService.getAddressByUserId(userId).log();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Address>> getAddressById(@PathVariable String id) {

        return addressService.getAddressById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Address> addAddress(@RequestBody @Valid Address address) {

        return addressService.addAddress(address).log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Address>> updateAddress(@RequestBody Address updatedAddress,
                                                     @PathVariable String id) {

        return addressService.updateAddress(updatedAddress, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAddress(@PathVariable String id) {

        return addressService.deleteAddress(id);
    }
}
