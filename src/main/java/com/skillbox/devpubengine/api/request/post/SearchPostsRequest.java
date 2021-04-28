package com.skillbox.devpubengine.api.request.post;

public class SearchPostsRequest {

    private int offset = 0;

    private int limit = 10;

    private String query = "";

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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
