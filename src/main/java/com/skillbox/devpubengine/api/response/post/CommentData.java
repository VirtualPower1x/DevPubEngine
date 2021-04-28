package com.skillbox.devpubengine.api.response.post;

public class CommentData {

    private int id;

    private long timestamp;

    private String text;

    private UserWithPhotoData user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserWithPhotoData getUser() {
        return user;
    }

    public void setUser(UserWithPhotoData user) {
        this.user = user;
    }
}
