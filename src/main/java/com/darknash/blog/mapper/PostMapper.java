package com.darknash.blog.mapper;

import com.darknash.blog.dto.CreatePostRequest;
import com.darknash.blog.dto.PostResponse;
import com.darknash.blog.dto.UpdatePostRequest;
import com.darknash.blog.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
        @Mapping(target = "author", source = "author")
        @Mapping(target = "category", source = "category")
        @Mapping(target = "tags", source = "tags")
        @Mapping(target = "status", source = "status")
        PostResponse toDto(Post post);

        CreatePostRequest toCreateRequest(CreatePostRequest request);

        UpdatePostRequest toUpdateRequest(UpdatePostRequest request);
}
