package com.ecom.controller;

import com.ecom.client.CategoryRestClient;
import com.ecom.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRestClient categoryRestClient;

    @GetMapping
    public Flux<Category> getAllCategories() {

        return categoryRestClient.retrieveCategories();
    }

    @GetMapping("{id}")
    public Mono<Category> getCategoryById(@PathVariable String categoryId) {

        return categoryRestClient.retrieveCategoryById(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Category> addCategory(@RequestBody @Valid Category category) {

        return categoryRestClient.saveCategory(category);
    }

    @PutMapping("{id}")
    public Mono<Category> updateCategory(@RequestBody Category updatedCategory,
                                                       @PathVariable String id) {

        return categoryRestClient.updateCategory(updatedCategory, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable String id) {

        categoryRestClient.deleteCategory(id);
    }
}
