package com.skillbox.devpubengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    @Override
    public String toString() {
        return "The user is either unauthorized or has no privileges for the requested action.";
    }

    public UnauthorizedException() {
        super("The user is either unauthorized or has no privileges for the requested action.");
    }
}
