package com.skillbox.devpubengine.model;

public enum Permission {

    USER("user:write"),
    MODERATE("user:moderate");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
