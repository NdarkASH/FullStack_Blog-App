package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.CreateTagRequest;
import com.darknash.blog.dto.TagResponse;
import com.darknash.blog.mapper.TagMapper;
import com.darknash.blog.model.Tag;
import com.darknash.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiPaths.TAGS)
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<List<TagResponse>> createTags(@RequestBody CreateTagRequest request) {
        List<Tag> tags = tagService.createTags(request.getName());
        List<TagResponse> tagResponses = tags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());

        return AppResponse.<List<TagResponse>>builder()
                .msg(HttpStatus.CREATED.toString())
                .code(HttpStatus.CREATED.value())
                .data(tagResponses)
                .build();

    }
}
