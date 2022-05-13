package com.ecom.controller;

import com.ecom.domain.Product;
import com.ecom.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/v1/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getAllProducts(@RequestParam(value = "category", required = false) String categoryId) {

        if(categoryId != null) {
            return productService.getProductByCategory(categoryId).log();
        }
        return productService.getAllProducts().log();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable String id) {

        return productService.getProductById(id)
                        .map(ResponseEntity.ok()::body)
                        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> addProduct(@RequestBody @Valid Product product) {

        return productService.addProduct(product).log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody Product updatedProduct,
                                                         @PathVariable String id) {

        return productService.updateProduct(updatedProduct, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String id) {

        return productService.deleteProduct(id);
    }
}