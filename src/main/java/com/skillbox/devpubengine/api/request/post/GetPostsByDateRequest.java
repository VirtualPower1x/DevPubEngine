package com.skillbox.devpubengine.api.request.post;

public class GetPostsByDateRequest {

    private int offset = 0;

    private int limit = 10;

    private String date = "";

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
