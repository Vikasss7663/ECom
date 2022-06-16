package com.ecom.controller;

import com.ecom.domain.Category;
import com.ecom.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.ecom.constants.ApplicationConstants.ORIGIN_URL;

@RestController
@CrossOrigin(origins = ORIGIN_URL)
@RequestMapping("/v1/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Flux<Category> getAllCategories() {

        return categoryService.getAllCategories().log();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Category>> getCategoryById(@PathVariable String id) {

        return categoryService.getCategoryById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Category> addCategory(@RequestBody @Valid Category category) {

        return categoryService.addCategory(category).log();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Category>> updateCategory(@RequestBody Category updatedCategory,
                                                           @PathVariable String id) {

        return categoryService.updateCategory(updatedCategory, id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCategory(@PathVariable String id) {

        return categoryService.deleteCategory(id);
    }
}