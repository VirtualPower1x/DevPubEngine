package com.skillbox.devpubengine.api.request.auth;

public class PasswordRestoreRequest {

    private String email;

    public PasswordRestoreRequest() {
    }

    public PasswordRestoreRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
