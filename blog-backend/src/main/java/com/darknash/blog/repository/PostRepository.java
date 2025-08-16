package com.darknash.blog.repository;

import com.darknash.blog.constant.PostStatus;
import com.darknash.blog.model.Category;
import com.darknash.blog.model.Post;
import com.darknash.blog.model.Tag;
import com.darknash.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tags);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
