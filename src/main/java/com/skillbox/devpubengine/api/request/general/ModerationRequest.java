package com.skillbox.devpubengine.api.request.general;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModerationRequest {

    @JsonProperty("post_id")
    private int postId;

    private String decision;

    public ModerationRequest() {

    }

    public ModerationRequest(int postId, String decision) {
        this.postId = postId;
        this.decision = decision;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
