package com.ecom.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // disable csrf
                .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/v1/product/**").hasRole("USER")
                .pathMatchers("/v1/product/**").hasRole("ADMIN")
                .anyExchange().authenticated().and()
                .httpBasic().and()
                .formLogin();
        return http.build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        var  user = User
                .withUsername("user")
                .password("password")
                .roles("USER")
                .build();

        var admin = User
                .withUsername("admin")
                .password("password")
                .roles("ADMIN", "USER")
                .build();

        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}