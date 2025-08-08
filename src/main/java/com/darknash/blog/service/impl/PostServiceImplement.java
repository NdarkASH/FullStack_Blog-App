package com.darknash.blog.service.impl;

import com.darknash.blog.constant.PostStatus;
import com.darknash.blog.model.Category;
import com.darknash.blog.model.Post;
import com.darknash.blog.model.Tag;
import com.darknash.blog.model.User;
import com.darknash.blog.repository.PostRepository;
import com.darknash.blog.service.CategoryService;
import com.darknash.blog.service.PostService;
import com.darknash.blog.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return postRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Post does not exist" + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
            if (categoryId != null && tagId != null) {
                Category category = categoryService.getCategoryById(categoryId);
                Tag tag = tagService.getTagById(tagId);
                return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                        PostStatus.PUBLISHED, category, tag
                );
            }
            if (categoryId != null) {
                Category category = categoryService.getCategoryById(categoryId);
                return postRepository.findAllByStatusAndCategory(
                        PostStatus.PUBLISHED,
                        category
                );
            }

            if (tagId != null) {
                Tag tag = tagService.getTagById(tagId);
                return postRepository.findAllByStatusAndTagsContaining(
                        PostStatus.PUBLISHED,
                        tag
                );
            }
            return postRepository.findAllByStatus(PostStatus.PUBLISHED);

    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(
                user,
                PostStatus.DRAFT
        );
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
