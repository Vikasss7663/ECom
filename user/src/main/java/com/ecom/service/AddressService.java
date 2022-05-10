package com.ecom.service;

import com.ecom.domain.Address;
import com.ecom.repository.AddressRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Flux<Address> getAddressByUserId(String userId) {
        return addressRepository.findByUserId(userId);
    }

    public Mono<Address> getAddressById(String id) {
        return  addressRepository.findById(id);
    }

    public Mono<Address> addAddress(Address address) {

        val date = new Date();
        address.setCreatedAt(date);
        address.setModifiedAt(date);
        return addressRepository.save(address);
    }

    public Mono<Address> updateAddress(Address updatedAddress, String id) {

        return addressRepository.findById(id)
                .flatMap(address -> {
                    address.setAddressLine1(updatedAddress.getAddressLine1());
                    address.setAddressLine2(updatedAddress.getAddressLine2());
                    address.setPostalCode(updatedAddress.getPostalCode());
                    address.setCity(updatedAddress.getCity());
                    address.setState(updatedAddress.getState());
                    address.setCountry(updatedAddress.getCountry());
                    address.setModifiedAt(new Date());
                    return addressRepository.save(address);
                });
    }

    public Mono<Void> deleteAddress(String id) {

        return addressRepository.deleteById(id);
    }

}