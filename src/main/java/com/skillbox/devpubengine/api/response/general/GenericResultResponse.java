package com.skillbox.devpubengine.api.response.general;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResultResponse {

    private boolean result;

    private Map<String, String> errors = null;

    public GenericResultResponse() {

    }

    public GenericResultResponse(boolean result) {
        this.result = result;
    }

    public GenericResultResponse(boolean result, Map<String, String> errors) {
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
