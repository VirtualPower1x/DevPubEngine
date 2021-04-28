package com.skillbox.devpubengine.api.response.general;

import java.util.HashMap;
import java.util.HashSet;

public class CalendarResponse {

    private HashSet<Integer> years;

    private HashMap<String, Integer> posts;

    public CalendarResponse() {
    }

    public CalendarResponse(HashSet<Integer> years, HashMap<String, Integer> posts) {
        this.years = years;
        this.posts = posts;
    }

    public HashSet<Integer> getYears() {
        return years;
    }

    public void setYears(HashSet<Integer> years) {
        this.years = years;
    }

    public HashMap<String, Integer> getPosts() {
        return posts;
    }

    public void setPosts(HashMap<String, Integer> posts) {
        this.posts = posts;
    }
}
