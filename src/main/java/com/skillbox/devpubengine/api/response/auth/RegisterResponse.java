package com.skillbox.devpubengine.api.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponse {

    private boolean result;

    private Map<String, String> errors = null;

    public RegisterResponse() {

    }

    public RegisterResponse(boolean result) {
        this.result = result;
    }

    public RegisterResponse(boolean result, Map<String, String> errors) {
        this.result = result;
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
