package com.skillbox.devpubengine.exception;

import com.skillbox.devpubengine.api.response.auth.LoginResponse;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DevPubEngineExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(RuntimeException ex,
                                                                          WebRequest request) {
        LoginResponse response = new LoginResponse(false);
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(
            RuntimeException ex, WebRequest request, NotFoundException e) {
        String bodyOfResponse = e.toString();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(
            RuntimeException ex, WebRequest request, UnauthorizedException e) {
        String bodyOfResponse = e.toString();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(FileUploadException.class)
    protected ResponseEntity<Object> handleFileUploadException(
            RuntimeException ex, WebRequest request, FileUploadException e) {
        GenericResultResponse body = e.getResponse();
        return handleExceptionInternal(ex, body,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
