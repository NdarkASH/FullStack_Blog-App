package com.darknash.blog.controller;

import com.darknash.blog.dto.AppResponse;
import com.darknash.blog.dto.PostResponse;
import com.darknash.blog.mapper.PostMapper;
import com.darknash.blog.model.Post;
import com.darknash.blog.model.User;
import com.darknash.blog.service.PostService;
import com.darknash.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "#{@apiPaths.post}")
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
    public AppResponse<List<PostResponse>> getDraftPost(@RequestAttribute UUID postId) {
        User loggedInUser = userService.getUserById(postId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostResponse> postResponses = draftPosts.stream()
                .map(postMapper::toDto)
                .toList();
        return AppResponse.
                <List<PostResponse>>builder()
                .code(HttpStatus.OK.value())
                .msg(HttpStatus.OK.getReasonPhrase())
                .data(postResponses)
                .build();
    }




}
