package com.ecom.service;

import com.ecom.domain.Product;
import com.ecom.repository.ProductRepository;
import com.ecom.schema.product.ProductEvent;
import com.ecom.schema.product.ProductEventKey;
import com.ecom.schema.product.ProductStatusEnum;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import static com.ecom.constants.ApplicationConstants.TOPIC_NAME;

@Slf4j
@Service
public class ProductService {

    private ProductRepository productRepository;
    private ReactiveCircuitBreaker productServiceCircuitBreaker;
    private MeterRegistry meterRegistry;
    private KafkaProducer<ProductEventKey, ProductEvent> producer;

    public ProductService(ProductRepository productRepository, ReactiveCircuitBreaker productServiceCircuitBreaker, MeterRegistry meterRegistry, KafkaProducer<ProductEventKey, ProductEvent> producer) {
        this.productRepository = productRepository;
        this.productServiceCircuitBreaker = productServiceCircuitBreaker;
        this.meterRegistry = meterRegistry;
        this.producer = producer;
    }

    public Flux<Product> getAllProducts() {

        Product defaultProduct1 = new Product("1", "Default Product-1", 12000d, "1", new Date(), new Date());
        Product defaultProduct2 = new Product("2", "Default Product-2", 12000d, "1", new Date(), new Date());
        Product defaultProduct3 = new Product("3", "Default Product-3", 12000d, "1", new Date(), new Date());

        return productServiceCircuitBreaker.run(
                productRepository.findAll().map(products -> {
                    meterRegistry.counter("product.fetch.details", "outcome", "success").increment();
                    return products;
                }),
                throwable -> {
                    meterRegistry.counter("product.fetch.details", "outcome", "failure").increment();
                    return Flux.just(defaultProduct1, defaultProduct2, defaultProduct3);
                }
        );
    }

    public Mono<Product> getProductById(String id) {

        Product defaultProduct = new Product("1", "Default Product", 12000d, "1", new Date(), new Date());

        return  productServiceCircuitBreaker.run(productRepository.findById(id),
                throwable -> Mono.just(defaultProduct)
        );
    }

    public Mono<Product> addProduct(Product product) {

        val date = new Date();

        Product defaultProduct = new Product("1", "Default Product", 12000d, "1", date, date);

        product.setCreatedAt(date);
        product.setModifiedAt(date);
        return productServiceCircuitBreaker.run(
                productRepository.save(product).map(savedProduct -> {
                    sendProductEvent(savedProduct, ProductStatusEnum.CREATED);
                    return savedProduct;
                }),
                throwable -> Mono.just(defaultProduct)
        );
    }

    public Mono<Product> updateProduct(Product updatedProduct, String id) {

        val date = new Date();

        Product defaultProduct = new Product("1", "Default Product", 12000d, "1", date, date);

        updatedProduct.setModifiedAt(date);

        return productServiceCircuitBreaker.run(
                productRepository.findById(id)
                        .flatMap(product -> {
                            product.setProductPrice(updatedProduct.getProductPrice());
                            product.setProductName(updatedProduct.getProductName());
                            product.setCategoryId(updatedProduct.getCategoryId());
                            product.setModifiedAt(new Date());
                            return productRepository.save(product).map(updatedProductMono -> {
                                sendProductEvent(updatedProductMono, ProductStatusEnum.UPDATED);
                                return updatedProductMono;
                            });
                        }),
                throwable -> Mono.just(defaultProduct)
        );
    }

    public Mono<Void> deleteProduct(String id) {

        return productServiceCircuitBreaker.run(
                productRepository.deleteById(id),
                throwable -> Mono.empty());
    }


    public Flux<Product> getProductByCategory(String categoryId) {

        Product defaultProduct1 = new Product("1", "Default Product-1", 12000d, categoryId, new Date(), new Date());
        Product defaultProduct2 = new Product("2", "Default Product-2", 12000d, categoryId, new Date(), new Date());

        return productServiceCircuitBreaker.run(productRepository.findByCategoryId(categoryId),
                throwable -> Flux.just(defaultProduct1, defaultProduct2)
        );
    }

    private void sendProductEvent(Product product, ProductStatusEnum status) {

        log.debug("Sending Product Event");

        ProductEventKey productEventKey = new ProductEventKey();
        ProductEvent productEvent = new ProductEvent();

        productEventKey.setProductId(product.getProductId());
        productEventKey.setProductName(product.getProductName());
        productEventKey.setCategoryId(product.getCategoryId());

        productEvent.setProductId(product.getProductId());
        productEvent.setProductName(product.getProductName());
        productEvent.setProductPrice(product.getProductPrice());
        productEvent.setCategoryId(product.getCategoryId());
        productEvent.setProductStatus(status);

        ProducerRecord<ProductEventKey, ProductEvent> record
                = new ProducerRecord<>(TOPIC_NAME, productEventKey, productEvent);

        producer.send(record);
    }
}