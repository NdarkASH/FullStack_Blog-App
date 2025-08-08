package com.darknash.blog.service;

import com.darknash.blog.model.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagName);
    void deleteTag(UUID tagsId);
    Tag getTagById(UUID tagId);
    List<Tag> getTagsByIds(Set<UUID> tagName);
}
