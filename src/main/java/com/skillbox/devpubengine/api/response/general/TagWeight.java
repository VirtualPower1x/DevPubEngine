package com.skillbox.devpubengine.api.response.general;

public class TagWeight {

    private String name;

    private double weight;

    public TagWeight() {
    }

    public TagWeight(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
