package com.darknash.blog.controller;

import com.darknash.blog.constant.ApiPaths;
import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.CreatePostRequest;
import com.darknash.blog.dto.PostResponse;
import com.darknash.blog.dto.UpdatePostRequest;
import com.darknash.blog.mapper.PostMapper;
import com.darknash.blog.model.Post;
import com.darknash.blog.model.User;
import com.darknash.blog.service.PostService;
import com.darknash.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = ApiPaths.POSTS)
@RequiredArgsConstructor
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;




    @GetMapping
    public AppResponse<List<PostResponse>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
            ) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostResponse> postResponses = posts
                .stream()
                .map(postMapper::toDto)
                .toList();

        return AppResponse.<List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .msg(HttpStatus.OK.getReasonPhrase())
                .data(postResponses)
                .build();
    }

    @GetMapping(path = "/drafts")
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<List<PostResponse>> getDraftPost(@RequestAttribute UUID userId) {
        log.warn("getDraftPost");
        User loggedInUser = userService.getUserById(userId);
        log.info("loggedInUser: {}", loggedInUser);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        log.info("draftPosts: {}", draftPosts);
        List<PostResponse> postResponses = draftPosts.stream()
                .map(postMapper::toDto)
                .toList();
        log.info("postResponses: {}", postResponses);
        return AppResponse.
                <List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .msg(HttpStatus.OK.getReasonPhrase())
                .data(postResponses)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppResponse<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreateRequest(request);
        Post post = postService.createPost(loggedInUser, createPostRequest);
        PostResponse postResponse = postMapper.toDto(post);
        return AppResponse.<PostResponse>builder()
                .code(HttpStatus.CREATED.value())
                .msg(HttpStatus.CREATED.getReasonPhrase())
                .data(postResponse)
                .build();
    }

    @PutMapping(path = "/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<PostResponse> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdateRequest(request);
        Post updatePost = postService.updatePost(id, updatePostRequest);
        PostResponse savedUpdatePost = postMapper.toDto(updatePost);
        return AppResponse.<PostResponse>
                builder()
                .code(HttpStatus.OK.value())
                .msg(HttpStatus.OK.getReasonPhrase())
                .data(savedUpdatePost)
                .build();
    }

    @GetMapping(path = "/{id}")
    public AppResponse<PostResponse> getPostById(@PathVariable UUID id) {
        Post post = postService.getPost(id);
        PostResponse postResponse = postMapper.toDto(post);
        return AppResponse.<PostResponse>builder()
                .code(HttpStatus.OK.value())
                .msg(HttpStatus.OK.getReasonPhrase())
                .data(postResponse)
                .build();
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppResponse<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return AppResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .msg("Category deleted")
                .build();
    }




}
