package com.skillbox.devpubengine.api.response.post;

import java.util.Collections;
import java.util.List;

public class PostsResponse {

    private int count = 0;

    private List<PostData> posts = Collections.emptyList();

    public PostsResponse() {

    }

    public PostsResponse(int count, List<PostData> posts) {
        this.count = count;
        this.posts = posts;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PostData> getPosts() {
        return posts;
    }

    public void setPosts(List<PostData> posts) {
        this.posts = posts;
    }
}
