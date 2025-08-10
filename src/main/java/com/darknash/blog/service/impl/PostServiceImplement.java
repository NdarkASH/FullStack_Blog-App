package com.darknash.blog.service.impl;

import com.darknash.blog.constant.PostStatus;
import com.darknash.blog.dto.CreatePostRequest;
import com.darknash.blog.dto.UpdatePostRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Post createPost(User user, CreatePostRequest request) {
        Post post = new Post();
        post.setAuthor(user);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setStatus(request.getStatus());
        post.setCategory(categoryService.getCategoryById(request.getCategoryId()));
        post.setReadingTime(calculateReadingTIme(request.getContent()));
        Set<UUID> tagsIds = request.getTags();
        List<Tag> tags = tagService.getTagsByIds(tagsIds);
        post.setTags(new HashSet<>(tags));
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(UUID uuid, UpdatePostRequest request) {
        Post existingPost = getPost(uuid);

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setStatus(request.getStatus());
        existingPost.setReadingTime(calculateReadingTIme(request.getContent()));

        UUID updatePostCategoryId = request.getCategoryId();
        if (!existingPost.getCategory().getId().equals(updatePostCategoryId)) {
            Category category = categoryService.getCategoryById(updatePostCategoryId);
            existingPost.setCategory(category);
        }
//        looping untuk mencari tags yang ada
        Set<UUID> existingTagIds = existingPost.getTags()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
        Set<UUID> updatedPostRequestAndTagsIds = request.getTagIds();
        if (!existingTagIds.containsAll(updatedPostRequestAndTagsIds)) {
            List<Tag> newTags = tagService.getTagsByIds(updatedPostRequestAndTagsIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }


    private Integer calculateReadingTIme(String content){
        if (content == null || content.isEmpty()){
            return null;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
