package com.skillbox.devpubengine.api.request.post;

public class GetPostsForModerationRequest {

    private int offset = 0;

    private int limit = 10;

    private String status = "new";

    public GetPostsForModerationRequest() {
    }

    public GetPostsForModerationRequest(int offset, int limit, String status) {
        this.offset = offset;
        this.limit = limit;
        this.status = status;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
