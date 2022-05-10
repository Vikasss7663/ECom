package com.ecom.controller;

import com.ecom.domain.User;
import com.ecom.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> getAllUsers() {

        return userService.getAllUsers().log();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String id) {

        return userService.getUserById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> addUser(@RequestBody @Valid User user) {

        return userService.addUser(user).log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@RequestBody User updatedUser,
                                                     @PathVariable String id) {

        return userService.updateUser(updatedUser, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable String id) {

        return userService.deleteUser(id);
    }
}
