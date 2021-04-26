package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.response.general.TagWeight;
import com.skillbox.devpubengine.api.response.general.TagsResponse;
import com.skillbox.devpubengine.model.TagEntity;
import com.skillbox.devpubengine.repository.TagRepository;
import com.skillbox.devpubengine.service.post.GetPostsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagsService {
    private final TagRepository tagRepository;

    public TagsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagsResponse getTags (String query) {
        List<TagEntity> tagList;
        if (query == null || query.equals("")) {
            tagList = tagRepository.findAll();
        }
        else {
            tagList = tagRepository.findAll()
                    .stream()
                    .filter(e -> e.getName().startsWith(query))
                    .collect(Collectors.toList());
        }
        tagList.sort((t1, t2) -> Integer.compare(t2.getTag2PostEntities().size(), t1.getTag2PostEntities().size()));

        List<TagWeight> tagWeights = new ArrayList<>();
        double postCount = GetPostsService.getPostsCount();
        if (tagList.isEmpty()) {
            return new TagsResponse(tagWeights);
        }
        double maxWeight = tagList.get(0).getTag2PostEntities().size() / postCount;
        for (TagEntity tag : tagList) {
            double weightNotRound = tag.getTag2PostEntities().size() / postCount / maxWeight;
            double weight = Math.round(weightNotRound * 100) / 100.0;
            TagWeight tagWeight = new TagWeight(tag.getName(), weight);
            tagWeights.add(tagWeight);
        }
        return new TagsResponse(tagWeights);
    }
}
