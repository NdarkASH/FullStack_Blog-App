package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.CreateTagRequest;
import com.darknash.blog.dto.TagResponse;
import com.darknash.blog.mapper.TagMapper;
import com.darknash.blog.model.Tag;
import com.darknash.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiPaths.TAGS)
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<List<TagResponse>> createTags(@RequestBody CreateTagRequest request) {
        log.info("Create tag request: {}", request);
        List<Tag> tags = tagService.createTags(request.getNames());
        List<TagResponse> tagResponses = tags.stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());

        return AppResponse.<List<TagResponse>>builder()
                .msg(HttpStatus.CREATED.toString())
                .code(HttpStatus.CREATED.value())
                .data(tagResponses)
                .build();

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<List<TagResponse>> getAllTags() {
        List<Tag> tags = tagService.getTags();
        List<TagResponse> tagResponses = tags.stream().map(tagMapper::toDto).toList();

        return AppResponse.<List<TagResponse>>builder()
                .msg(HttpStatus.OK.toString())
                .code(HttpStatus.OK.value())
                .data(tagResponses)
                .build();
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AppResponse<Void> deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
        return AppResponse.<Void>builder().build();
    }
}
