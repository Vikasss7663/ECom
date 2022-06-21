package com.ecom.service;

import com.ecom.domain.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.schema.product.ProductEvent;
import com.ecom.schema.product.ProductEventKey;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ProductService {

    private ProductRepository productRepository;
    private ReactiveCircuitBreaker productServiceCircuitBreaker;
    private MeterRegistry meterRegistry;
    private KafkaProducer<ProductEventKey, ProductEvent> producer;

    public ProductService(ProductRepository productRepository, ReactiveCircuitBreakerFactory circuitBreakerFactory, MeterRegistry meterRegistry, KafkaProducer<ProductEventKey, ProductEvent> producer) {
        this.productRepository = productRepository;
        this.productServiceCircuitBreaker = circuitBreakerFactory.create("productServiceCircuitBreaker");
        this.meterRegistry = meterRegistry;
        this.producer = producer;
    }

    public Flux<Product> getAllProducts() {

        return productServiceCircuitBreaker.run(
                productRepository.findAll()
                        .doOnNext(products ->
                                meterRegistry.counter("product.fetch.details", "outcome", "success").increment()
                        ),
                throwable -> {
                    meterRegistry.counter("product.fetch.details", "outcome", "failure").increment();
                    return Flux.error(throwable);
                }
        );
    }

    public Mono<Product> getProductById(String id) {

        return productServiceCircuitBreaker.run(
                productRepository.findById(id)
                        .doOnNext(product ->
                                meterRegistry.counter("product.fetch.id", "outcome", "success").increment()
                        ),
                throwable -> {
                    meterRegistry.counter("product.fetch.id", "outcome", "failure").increment();
                    return Mono.error(throwable);
                }
        );
    }

    public Mono<Product> addProduct(Product product) {

        val date = LocalDate.now();

        product.setCreatedAt(date);
        product.setModifiedAt(date);
        return productServiceCircuitBreaker.run(
                productRepository.save(product).doOnNext(savedProductMono -> {
                    meterRegistry.counter("product.add", "outcome", "success").increment();
//                    ProductProducer.sendProductEvent(producer, savedProductMono);
                }),
                throwable -> {
                    meterRegistry.counter("product.add", "outcome", "failure").increment();
                    return Mono.error(throwable);
                }
        );
    }

    public Mono<Product> updateProduct(Product updatedProduct, String id) {

        return productServiceCircuitBreaker.run(
                productRepository.findById(id)
                        .flatMap(product -> {
                            product.setProductPrice(updatedProduct.getProductPrice());
                            product.setProductName(updatedProduct.getProductName());
                            product.setProductDesc(updatedProduct.getProductDesc());
                            product.setCategoryId(updatedProduct.getCategoryId());
                            product.setProductImageUrl(updatedProduct.getProductImageUrl());
                            product.setProductQuantity(updatedProduct.getProductQuantity());
                            product.setModifiedAt(LocalDate.now());
                            return productRepository.save(product).doOnNext(updatedProductMono -> {
                                meterRegistry.counter("product.update", "outcome", "success").increment();
//                                ProductProducer.sendProductEvent(producer, updatedProductMono);
                            });
                        }),
                (throwable) -> {
                    meterRegistry.counter("product.update", "outcome", "failure").increment();
                    return Mono.error(throwable);
                }
        );
    }

    public Mono<Void> deleteProduct(String id) {

        return productServiceCircuitBreaker.run(
                productRepository.deleteById(id).doOnNext(products -> meterRegistry.counter("product.delete", "outcome", "success").increment()),
                throwable -> {
                    meterRegistry.counter("product.delete", "outcome", "failure").increment();
                    return Mono.error(throwable);
                });
    }


    public Flux<Product> getProductByCategory(String categoryId) {

        return productServiceCircuitBreaker.run(
                productRepository.findByCategoryId(categoryId).doOnNext(products -> meterRegistry.counter("product.fetch.category", "outcome", "success").increment()),
                throwable -> {
                    meterRegistry.counter("product.fetch.category", "outcome", "failure").increment();
                    return Flux.error(throwable);
                }
        );
    }

    public Flux<Product> getProductByIds(Set<String> productIdList) {

        return productServiceCircuitBreaker.run(
                productRepository.findAllById(productIdList).doOnNext(products -> meterRegistry.counter("product.fetch.ids", "outcome", "success").increment()),
                throwable -> {
                    meterRegistry.counter("product.fetch.ids", "outcome", "failure").increment();
                    return Flux.error(throwable);
                }
        );
    }

    public Flux<Product> updateProducts(List<Product> productList) {

        return productServiceCircuitBreaker.run(
                productRepository.saveAll(productList).doOnNext(products -> meterRegistry.counter("product.update.products", "outcome", "success").increment()),
                throwable -> {
                    meterRegistry.counter("product.update.products", "outcome", "failure").increment();
                    return Flux.error(throwable);
                }
        );
    }
}