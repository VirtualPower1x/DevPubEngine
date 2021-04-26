package com.skillbox.devpubengine.api.response.post;

import java.util.Collections;
import java.util.List;

public class GetPostsResponse {

    private int count = 0;

    private List<PostData> posts = Collections.emptyList();

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
