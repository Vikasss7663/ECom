package com.ecom.controller;

import com.ecom.client.UserRestClient;
import com.ecom.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRestClient userRestClient;

    @GetMapping
    public Flux<User> getAllUsers() {

        return userRestClient.retrieveUsers();
    }

    @GetMapping("{id}")
    public Mono<User> getUserById(@PathVariable String userId) {

        return userRestClient.retrieveUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> addUser(@RequestBody @Valid User user) {

        return userRestClient.saveUser(user);
    }

    @PutMapping("{id}")
    public Mono<User> updateUser(@RequestBody User updatedUser,
                                                       @PathVariable String id) {

        return userRestClient.updateUser(updatedUser, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {

        userRestClient.deleteUser(id);
    }
}
