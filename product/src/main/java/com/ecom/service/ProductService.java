package com.ecom.service;

import com.ecom.domain.Product;
import com.ecom.repository.ProductRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> getProductById(String id) {
        return  productRepository.findById(id);
    }

    public Mono<Product> addProduct(Product product) {

        val date = new Date();
        product.setCreatedAt(date);
        product.setModifiedAt(date);
        return productRepository.save(product);
    }

    public Mono<Product> updateProduct(Product updatedProduct, String id) {

        return productRepository.findById(id)
                .flatMap(product -> {
                    product.setProductPrice(updatedProduct.getProductPrice());
                    product.setProductName(updatedProduct.getProductName());
                    product.setCategoryId(updatedProduct.getCategoryId());
                    product.setModifiedAt(new Date());
                    return productRepository.save(product);
                });
    }

    public Mono<Void> deleteProduct(String id) {

        return productRepository.deleteById(id);
    }


    public Flux<Product> getProductByCategory(String categoryId) {

        return productRepository.findByCategoryId(categoryId);
    }

}