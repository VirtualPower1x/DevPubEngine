package com.skillbox.devpubengine.api.request.post;

public class GetPostsRequest {

    private int offset = 0;

    private int limit = 10;

    private String mode = "recent";

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
