package com.ecom.controller;

import com.ecom.client.ProductRestClient;
import com.ecom.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRestClient productRestClient;

    @GetMapping
    public Flux<Product> getAllProducts(@RequestParam(value = "category", required = false) String categoryId) {

        return productRestClient.retrieveProducts(categoryId);
    }

    @GetMapping("{id}")
    public Mono<Product> getProductById(@PathVariable String productId) {

        return productRestClient.retrieveProductById(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> addProduct(@RequestBody @Valid Product product) {

        return productRestClient.saveProduct(product);
    }

    @PutMapping("{id}")
    public Mono<Product> updateProduct(@RequestBody Product updatedProduct,
                                                       @PathVariable String id) {

        return productRestClient.updateProduct(updatedProduct, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {

        productRestClient.deleteProduct(id);
    }
}
