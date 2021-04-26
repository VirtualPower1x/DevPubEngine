package com.skillbox.devpubengine.api.response.general;

import java.util.Collections;
import java.util.List;

public class TagsResponse {

    private List<TagWeight> tags = Collections.emptyList();

    public TagsResponse() {
    }

    public TagsResponse(List<TagWeight> tags) {
        this.tags = tags;
    }

    public List<TagWeight> getTags() {
        return tags;
    }

    public void setTags(List<TagWeight> tags) {
        this.tags = tags;
    }
}
