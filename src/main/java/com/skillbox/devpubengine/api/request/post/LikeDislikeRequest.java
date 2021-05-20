package com.skillbox.devpubengine.api.request.post;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LikeDislikeRequest {

    @JsonProperty("post_id")
    private int postId;

    public LikeDislikeRequest() {

    }

    public LikeDislikeRequest(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
