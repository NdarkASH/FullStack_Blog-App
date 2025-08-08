package com.darknash.blog.service;

import com.darknash.blog.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post getPost(UUID id);
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(UUID categoryId, UUID tagId);
    Post createPost(Post post);
    Post updatePost(Post post);
    void deletePost(UUID id);
}
