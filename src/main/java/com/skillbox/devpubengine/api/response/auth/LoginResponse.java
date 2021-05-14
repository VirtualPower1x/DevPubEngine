package com.skillbox.devpubengine.api.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private boolean result;

    private LoginUserData user = null;

    public LoginResponse() {

    }

    public LoginResponse(boolean result) {
        this.result = result;
    }

    public LoginResponse(boolean result, LoginUserData user) {
        this.result = result;
        this.user = user;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LoginUserData getUser() {
        return user;
    }

    public void setUser(LoginUserData user) {
        this.user = user;
    }
}
