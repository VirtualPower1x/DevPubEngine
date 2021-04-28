package com.skillbox.devpubengine.api.request.post;

public class GetPostsByTagRequest {

    private int offset = 0;

    private int limit = 10;

    private String tag = "";

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
