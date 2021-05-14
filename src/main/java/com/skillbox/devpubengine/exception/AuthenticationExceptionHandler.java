package com.skillbox.devpubengine.exception;

import com.skillbox.devpubengine.api.response.auth.LoginResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AuthenticationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(RuntimeException ex,
                                                                          WebRequest request) {
        LoginResponse response = new LoginResponse(false);
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.OK, request);
    }
}
