package com.darknash.blog.repository;

import com.darknash.blog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @Query("SELECT c FROM Category c left JOIN FETCH c.posts")
    List<Category> findAllWithPostCount();

    Boolean existsByName(String name);
}
