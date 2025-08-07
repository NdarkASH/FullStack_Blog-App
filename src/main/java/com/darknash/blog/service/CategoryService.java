package com.darknash.blog.service;

import com.darknash.blog.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategory();
    Category crateCategory(Category category);
    void deleteCategory(UUID uuid);
    Category getCategoryById(UUID id);
}
