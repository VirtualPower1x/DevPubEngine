package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.auth.LoginRequest;
import com.skillbox.devpubengine.api.request.auth.RegisterRequest;
import com.skillbox.devpubengine.api.response.auth.CaptchaResponse;
import com.skillbox.devpubengine.api.response.auth.LoginResponse;
import com.skillbox.devpubengine.api.response.auth.RegisterResponse;
import com.skillbox.devpubengine.service.auth.CaptchaService;
import com.skillbox.devpubengine.service.auth.LoginService;
import com.skillbox.devpubengine.service.auth.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final LoginService loginService;

    public ApiAuthController(CaptchaService captchaService,
                             RegisterService registerService, LoginService loginService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponse> checkAuth(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new LoginResponse(false));
        }
        return ResponseEntity.ok(loginService.getResponse(principal.getName()));
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.authorize(request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(loginService.logout(request, response));
    }
}
