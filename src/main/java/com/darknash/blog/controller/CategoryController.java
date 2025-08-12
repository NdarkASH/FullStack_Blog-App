package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.CategoryResponse;
import com.darknash.blog.dto.CreateCategoryRequest;
import com.darknash.blog.mapper.CategoryMapper;
import com.darknash.blog.mapper.PostMapper;
import com.darknash.blog.model.Category;
import com.darknash.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiPaths.CATEGORIES)
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<List<CategoryResponse>> listCategory() {
        List<CategoryResponse> categories = categoryService.listCategory()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
        return AppResponse.<List<CategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .msg(HttpStatus.OK.getReasonPhrase())
                .data(categories)
                .build();
    }

    @PostMapping
    public AppResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryService.crateCategory(category);

        return AppResponse.<CategoryResponse>builder()
                .code(HttpStatus.CREATED.value())
                .msg(HttpStatus.CREATED.getReasonPhrase())
                .data(categoryMapper.toDto(savedCategory))
                .build();
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AppResponse<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);

        return AppResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .msg(HttpStatus.NO_CONTENT.getReasonPhrase())
                .build();
    }

}
