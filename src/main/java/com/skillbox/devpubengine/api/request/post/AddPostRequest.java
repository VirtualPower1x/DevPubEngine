package com.skillbox.devpubengine.api.request.post;

import java.util.List;

public class AddPostRequest {

    private long timestamp;

    private byte active;

    private String title;

    private List<String> tags;

    private String text;

    public AddPostRequest() {
    }

    public AddPostRequest(long timestamp, byte active, String title, List<String> tags, String text) {
        this.timestamp = timestamp;
        this.active = active;
        this.title = title;
        this.tags = tags;
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte getActive() {
        return active;
    }

    public void setActive(byte active) {
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
