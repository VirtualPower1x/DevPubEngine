package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.response.general.TagWeight;
import com.skillbox.devpubengine.api.response.general.TagsResponse;
import com.skillbox.devpubengine.model.TagEntity;
import com.skillbox.devpubengine.repository.TagRepository;
import com.skillbox.devpubengine.service.post.PostsService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
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
        if (query == null) {
            query = "";
        }
        final String queryFinal = query;
        List<TagEntity> tagList = tagRepository
                .findAll(JpaSort.unsafe(Sort.Direction.DESC, "size(tag2PostEntities)"))
                .stream()
                .filter(e -> e.getName().startsWith(queryFinal))
                .collect(Collectors.toList());

        List<TagWeight> tagWeights = new ArrayList<>();
        if (tagList.isEmpty()) {
            return new TagsResponse(tagWeights);
        }
        double postCount = PostsService.getPostsCount();
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
