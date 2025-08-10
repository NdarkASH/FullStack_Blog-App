package com.darknash.blog.service.impl;

import com.darknash.blog.model.Tag;
import com.darknash.blog.repository.TagRepository;
import com.darknash.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagsServiceImplement implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Override
    public List<Tag> createTags(Set<String> request) {
        List<Tag> existingTags = tagRepository.findByNameIn(request);

        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = request.stream()
                .filter(name-> !existingTagNames.contains(name))
                .map(
                        name-> {
                            Tag tag = new Tag();
                            tag.setName(name);
                            tag.setPosts(HashSet.newHashSet());
                            return tag;

                        }).toList();


        return List.of();
    }

    @Override
    public void deleteTag(UUID tagsId) {

    }

    @Override
    public Tag getTagById(UUID requestId) {
        return null;
    }

    @Override
    public List<Tag> getTagsByIds(Set<UUID> requestIds) {
        return List.of();
    }
}
