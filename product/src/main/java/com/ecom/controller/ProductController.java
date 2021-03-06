package com.ecom.controller;

import com.ecom.domain.Product;
import com.ecom.domain.ProductInventory;
import com.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@Slf4j
@RestController
@CrossOrigin(origins = ORIGIN_URL)
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final boolean productServiceLogEnabled = true;

    @GetMapping
    public Flux<Product> getAllProducts(@RequestParam(value = "category", required = false) String categoryId) {

        if(categoryId != null) {
            return productService.getProductByCategory(categoryId)
                    .onErrorResume(throwable -> {
                        if(productServiceLogEnabled) log.error(throwable.getMessage());
                        return Flux.empty();
                    });
        }

        return productService.getAllProducts()
                .onErrorResume(throwable -> {
                    if(productServiceLogEnabled) log.error(throwable.getMessage());
                    return Flux.empty();
                });
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable String id) {

        return productService.getProductById(id)
                        .map(ResponseEntity.ok()::body)
                        .onErrorResume(throwable -> {
                            if(productServiceLogEnabled) log.error(throwable.getMessage());
                            return Mono.empty();
                        })
                        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> addProduct(@RequestBody @Valid Product product) {

        return productService.addProduct(product).log();
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@RequestBody Product updatedProduct,
                                                         @PathVariable String id) {

        return productService.updateProduct(updatedProduct, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String id) {

        return productService.deleteProduct(id);
    }

    @PutMapping("/inc")
    public Flux<Product> increaseInventory(@RequestBody List<ProductInventory> productInventoryList) {

        return productService.increaseInventory(productInventoryList);
    }

    @PutMapping("/dec")
    public Flux<Product> decreaseInventory(@RequestBody List<ProductInventory> productInventoryList) {

        return productService.decreaseInventory(productInventoryList);
    }
}