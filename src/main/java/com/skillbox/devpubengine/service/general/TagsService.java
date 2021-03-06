package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.response.general.TagWeight;
import com.skillbox.devpubengine.api.response.general.TagsResponse;
import com.skillbox.devpubengine.model.TagEntity;
import com.skillbox.devpubengine.repository.TagRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class TagsService {

    private final TagRepository tagRepository;

    public TagsService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public TagsResponse getTags (String query) {
        if (query == null) {
            query = "";
        }
        final String queryFinal = query.toLowerCase(Locale.ROOT);
        List<TagEntity> tagList = tagRepository
                .findAll(JpaSort.unsafe(Sort.Direction.DESC, "size(tag2PostEntities)"))
                .stream()
                .filter(e -> e.getName().startsWith(queryFinal))
                .collect(Collectors.toList());

        List<TagWeight> tagWeights = new ArrayList<>();
        if (tagList.isEmpty()) {
            return new TagsResponse(tagWeights);
        }
        double maxWeight = tagList.get(0).getTag2PostEntities().size();
        for (TagEntity tag : tagList) {
            double weightNotRound = tag.getTag2PostEntities().size() / maxWeight;
            double weight = Math.round(weightNotRound * 100) / 100.0;
            TagWeight tagWeight = new TagWeight(tag.getName(), weight);
            tagWeights.add(tagWeight);
        }
        return new TagsResponse(tagWeights);
    }
}
