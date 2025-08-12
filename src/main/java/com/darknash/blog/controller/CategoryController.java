package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.CategoryResponse;
import com.darknash.blog.mapper.CategoryMapper;
import com.darknash.blog.mapper.PostMapper;
import com.darknash.blog.model.Category;
import com.darknash.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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




}
