package com.skillbox.devpubengine.api.request.general;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddCommentRequest {

    @JsonProperty("parent_id")
    private Integer parentId = null;

    @JsonProperty("post_id")
    private int postId;

    private String text;

    public AddCommentRequest() {

    }

    public AddCommentRequest(Integer parentId, int postId, String text) {
        this.parentId = parentId;
        this.postId = postId;
        this.text = text;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
