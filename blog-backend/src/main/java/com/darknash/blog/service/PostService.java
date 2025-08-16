package com.darknash.blog.service;

import com.darknash.blog.dto.CreatePostRequest;
import com.darknash.blog.dto.UpdatePostRequest;
import com.darknash.blog.model.Post;
import com.darknash.blog.model.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest request);
    Post updatePost(UUID id, UpdatePostRequest request);
    void deletePost(UUID id);
}
