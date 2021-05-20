package com.skillbox.devpubengine.api.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddCommentResponse {

    private boolean result;

    private Integer id = null;

    private Map<String, String> errors = null;

    public AddCommentResponse() {

    }

    public AddCommentResponse(boolean result, Integer id) {
        this.result = result;
        this.id = id;
    }

    public AddCommentResponse(boolean result, Map<String, String> errors) {
        this.result = result;
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
