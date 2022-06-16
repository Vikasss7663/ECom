package com.ecom.client;

import com.ecom.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRestClient {

    private final WebClient webClient;

    @Value("${restClient.userUrl}")
    private String userUrl;

    public Flux<User> retrieveUsers() {

        return webClient
                .get()
                .uri(userUrl)
                .retrieve()
                .bodyToFlux(User.class);
    }

    public Mono<User> retrieveUserById(String userId) {

        return webClient
                .get()
                .uri(userUrl + "/" + userId)
                .retrieve()
                .bodyToMono(User.class);
    }


    public Mono<User> saveUser(User user) {

        return webClient
                .post()
                .uri(userUrl)
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(User.class);
    }

    public Mono<User> updateUser(User user, String userId) {

        return webClient
                .put()
                .uri(userUrl + "/" + userId)
                .body(Mono.just(user), User.class)
                .retrieve()
                .bodyToMono(User.class);
    }

    public void deleteUser(String userId) {

        webClient
                .delete()
                .uri(userUrl + "/" + userId)
                .exchangeToMono(response -> Mono.empty())
                .subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}
