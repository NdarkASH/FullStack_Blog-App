package com.darknash.blog.service.impl;

import com.darknash.blog.model.Tag;
import com.darknash.blog.repository.TagRepository;
import com.darknash.blog.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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
                            tag.setPosts(new HashSet<>());
                            return tag;

                        }).toList();

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
        }
        List<Tag> resultTags = new ArrayList<>(existingTags);
        resultTags.addAll(newTags);
        return resultTags;
    }

    @Override
    public void deleteTag(UUID tagsId) {
        tagRepository.findById(tagsId).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalArgumentException("You can't delete tags that have no posts");
            }
            tagRepository.deleteById(tagsId);
        });

        tagRepository.deleteById(tagsId);

    }

    @Override
    public Tag getTagById(UUID requestId) {
        return tagRepository.findById(requestId)
                .orElseThrow(()-> new EntityNotFoundException("No tag found with id: " + requestId));
    }

    @Override
    public List<Tag> getTagsByIds(Set<UUID> requestIds) {
        List<Tag> foundTags = tagRepository.findAllById(requestIds);
        if (foundTags.size() != requestIds.size()) {
            throw new EntityNotFoundException("No tags found with ids: " + requestIds);
        }
        return foundTags;
    }
}
