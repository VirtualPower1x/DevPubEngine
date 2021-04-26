package com.skillbox.devpubengine.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuth() {
        // TODO: complete auth response & service classes
        // TODO: configure this method to return current user data
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
