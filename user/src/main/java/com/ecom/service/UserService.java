package com.ecom.service;

import com.ecom.domain.User;
import com.ecom.repository.UserRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Mono<User> getUserById(String userId) {

        return userRepository.findById(userId);
    }

    public Mono<User> loginUser(String email, String password) {
        return userRepository.findByUserEmailAndUserPassword(email, password)
                .elementAt(0, new User());
    }

    public Mono<User> addUser(User user) {

        val date = LocalDate.now();
        user.setCreatedAt(date);
        user.setModifiedAt(date);
        return userRepository.save(user);
    }

    public Mono<User> updateUser(User updatedUser, String id) {

        return userRepository.findById(id)
                .flatMap(user -> {
                    user.setUserName(updatedUser.getUserName());
                    user.setUserEmail(updatedUser.getUserEmail());
                    user.setUserPassword(updatedUser.getUserPassword());
                    user.setUserPhone(updatedUser.getUserPhone());
                    user.setModifiedAt(LocalDate.now());
                    return userRepository.save(user);
                });
    }

    public Mono<Void> deleteUser(String id) {

        return userRepository.deleteById(id);
    }

}