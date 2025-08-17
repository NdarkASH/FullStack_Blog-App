package com.darknash.blog.service.impl;

import com.darknash.blog.exception.DuplicateEntity;
import com.darknash.blog.model.Category;
import com.darknash.blog.repository.CategoryRepository;
import com.darknash.blog.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImplement implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    public List<Category> listCategory() {

        return categoryRepository.findAllWithPostCount();
    }

    @Override
    public Category crateCategory(Category category) {

        String categoryName = category.getName();
        if (categoryRepository.existsByName(categoryName)) {
            throw new DuplicateEntity("Category with name " + categoryName + " already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID uuid) {
        Category category = categoryRepository.findById(uuid)
                .orElseThrow(()-> new EntityNotFoundException("Category not found"));
        log.info("Deleting category: " + category.getName());
        categoryRepository.delete(category);


    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
}
