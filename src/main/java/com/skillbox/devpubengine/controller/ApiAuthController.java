package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.auth.RegisterRequest;
import com.skillbox.devpubengine.api.response.auth.CaptchaResponse;
import com.skillbox.devpubengine.api.response.auth.RegisterResponse;
import com.skillbox.devpubengine.service.auth.CaptchaService;
import com.skillbox.devpubengine.service.auth.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;

    private final RegisterService registerService;

    public ApiAuthController(CaptchaService captchaService, RegisterService registerService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuth() {
        // TODO: complete auth response & service classes
        // TODO: configure this method to return current user data
        return ResponseEntity.ok(false);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        captchaService.deleteOutdatedCaptcha();
        return ResponseEntity.ok(captchaService.getCaptcha());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(registerService.register(request));
    }
}
