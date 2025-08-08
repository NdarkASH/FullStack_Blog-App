package com.darknash.blog.service.impl;

import com.darknash.blog.model.Tag;
import com.darknash.blog.repository.TagRepository;
import com.darknash.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TagsServiceImplement implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    public List<Tag> createTags(Set<String> tagName) {
        return List.of();
    }

    @Override
    public void deleteTag(UUID tagsId) {

    }

    @Override
    public Tag getTagById(UUID tagId) {
        return null;
    }

    @Override
    public List<Tag> getTagsByIds(Set<UUID> tagName) {
        return List.of();
    }
}
