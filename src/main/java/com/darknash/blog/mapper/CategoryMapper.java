package com.darknash.blog.mapper;

import com.darknash.blog.constant.PostStatus;
import com.darknash.blog.dto.CategoryResponse;
import com.darknash.blog.dto.CreateCategoryRequest;
import com.darknash.blog.model.Category;
import com.darknash.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryResponse toDto(Category category);

    Category toEntity(CreateCategoryRequest request);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }
}


