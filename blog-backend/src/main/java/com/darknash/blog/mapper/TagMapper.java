package com.darknash.blog.mapper;

import com.darknash.blog.constant.PostStatus;
import com.darknash.blog.dto.TagResponse;
import com.darknash.blog.model.Post;
import com.darknash.blog.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    @Mapping(target = "tagId", source = "uuid")
    @Mapping(target = "tagName", source = "name")
    TagResponse toDto(Tag tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return 0;
        }
        return (int) posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus())).count();
    }


}
