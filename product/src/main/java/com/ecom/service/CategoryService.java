package com.ecom.service;

import com.ecom.domain.Category;
import com.ecom.repository.CategoryRepository;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Flux<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Mono<Category> getCategoryById(String id) {
        return  categoryRepository.findById(id);
    }

    public Mono<Category> addCategory(Category category) {

        val date = new Date();
        category.setCreatedAt(date);
        category.setModifiedAt(date);
        return categoryRepository.save(category);
    }

    public Mono<Category> updateCategory(Category updatedCategory, String id) {

        return categoryRepository.findById(id)
                .flatMap(category -> {
                    category.setCategoryName(updatedCategory.getCategoryName());
                    category.setCategoryDesc(updatedCategory.getCategoryDesc());
                    category.setModifiedAt(new Date());
                    return categoryRepository.save(category);
                });
    }

    public Mono<Void> deleteCategory(String id) {

        return categoryRepository.deleteById(id);
    }

}