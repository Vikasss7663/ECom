package com.ecom.service;

import com.ecom.domain.Category;
import com.ecom.repository.CategoryRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.val;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ReactiveCircuitBreaker categoryServiceCircuitBreaker;
    private MeterRegistry meterRegistry;


    public CategoryService(CategoryRepository categoryRepository, ReactiveCircuitBreakerFactory circuitBreakerFactory, MeterRegistry meterRegistry) {
        this.categoryRepository = categoryRepository;
        this.categoryServiceCircuitBreaker = circuitBreakerFactory.create("categoryServiceCircuitBreaker");
        this.meterRegistry = meterRegistry;
    }

    public Flux<Category> getAllCategories() {

        Category defaultCategory1 = new Category("1", "Default Category-1", "Default Category Description", new Date(), new Date());
        Category defaultCategory2 = new Category("2", "Default Category-2", "Default Category Description", new Date(), new Date());
        Category defaultCategory3 = new Category("3", "Default Category-3", "Default Category Description", new Date(), new Date());

        return categoryServiceCircuitBreaker.run(
                categoryRepository.findAll().map(categories -> {
                    meterRegistry.counter("category.fetch.details", "outcome", "success").increment();
                    return categories;
                }),
                throwable -> {
                    meterRegistry.counter("category.fetch.details", "outcome", "failure").increment();
                    return Flux.just(defaultCategory1, defaultCategory2, defaultCategory3);
                });
    }

    public Mono<Category> getCategoryById(String id) {

        Category defaultCategory = new Category(id, "Default Category", "Default Category Description", new Date(), new Date());

        return  categoryServiceCircuitBreaker.run(categoryRepository.findById(id),
                throwable -> Mono.just(defaultCategory));
    }

    public Mono<Category> addCategory(Category category) {

        val date = new Date();

        Category defaultCategory = new Category("1", "Default Category", "Default Category Description", date, date);

        category.setCreatedAt(date);
        category.setModifiedAt(date);
        return categoryServiceCircuitBreaker.run(categoryRepository.save(category),
                throwable -> Mono.just(defaultCategory));
    }

    public Mono<Category> updateCategory(Category updatedCategory, String id) {

        var date = new Date();
        Category defaultCategory = new Category("1", "Default Category", "Default Category Description", date, date);

        return categoryServiceCircuitBreaker.run(
                categoryRepository.findById(id)
                        .flatMap(category -> {
                            category.setCategoryName(updatedCategory.getCategoryName());
                            category.setCategoryDesc(updatedCategory.getCategoryDesc());
                            category.setModifiedAt(new Date());
                            return categoryRepository.save(category);
                        }),
                throwable -> Mono.just(defaultCategory)
        );
    }

    public Mono<Void> deleteCategory(String id) {

        return categoryServiceCircuitBreaker.run(categoryRepository.deleteById(id),
                throwable -> Mono.empty());
    }

}