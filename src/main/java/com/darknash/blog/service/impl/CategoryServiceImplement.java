package com.darknash.blog.service.impl;

import com.darknash.blog.model.Category;
import com.darknash.blog.repository.CategoryRepository;
import com.darknash.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID uuid) {
        Optional<Category> category = categoryRepository.findById(uuid);
        if (category.isPresent()) {
            if (!category.get().getPosts().isEmpty()) {
                throw new IllegalArgumentException("Category already exists with name: " + category.get().getName());
            }
            categoryRepository.delete(category.get());
        }
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
}
