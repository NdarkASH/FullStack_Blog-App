package com.darknash.blog.dto;

import com.darknash.blog.constant.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private UUID uuid;
    private String title;
    private String content;
    private AuthorResponse author;
    private CategoryResponse category;
    private Set<TagResponse> tagResponses;
    private Integer readingTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PostStatus status;

}
