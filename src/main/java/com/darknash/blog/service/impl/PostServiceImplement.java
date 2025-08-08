package com.darknash.blog.service.impl;

import com.darknash.blog.model.Post;
import com.darknash.blog.repository.PostRepository;
import com.darknash.blog.service.CategoryService;
import com.darknash.blog.service.PostService;
import com.darknash.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImplement implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public Post getPost(UUID id) {
        return null;
    }

    @Override
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        return List.of();
    }

    @Override
    public List<Post> getDraftPosts(UUID categoryId, UUID tagId) {
        return List.of();
    }

    @Override
    public Post createPost(Post post) {
        return null;
    }

    @Override
    public Post updatePost(Post post) {
        return null;
    }

    @Override
    public void deletePost(UUID id) {

    }


    private Integer calculateReadingTIme(String content){
        if (content == null || content.isEmpty()){
            return null;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
