package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.auth.LoginRequest;
import com.skillbox.devpubengine.api.request.auth.PasswordChangeRequest;
import com.skillbox.devpubengine.api.request.auth.PasswordRestoreRequest;
import com.skillbox.devpubengine.api.request.auth.RegisterRequest;
import com.skillbox.devpubengine.api.response.auth.CaptchaResponse;
import com.skillbox.devpubengine.api.response.auth.LoginResponse;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.service.auth.*;
import com.skillbox.devpubengine.service.general.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final CaptchaService captchaService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final SettingsService settingsService;
    private final PasswordChangeService passwordChangeService;
    private final PasswordRestoreService passwordRestoreService;

    public ApiAuthController(CaptchaService captchaService,
                             RegisterService registerService,
                             LoginService loginService,
                             SettingsService settingsService,
                             PasswordChangeService passwordChangeService,
                             PasswordRestoreService passwordRestoreService) {
        this.captchaService = captchaService;
        this.registerService = registerService;
        this.loginService = loginService;
        this.settingsService = settingsService;
        this.passwordChangeService = passwordChangeService;
        this.passwordRestoreService = passwordRestoreService;
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
    public ResponseEntity<GenericResultResponse> register(@RequestBody RegisterRequest request) {
        if (!settingsService.getSettings().isMultiuserMode()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registerService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginService.authorize(request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/logout")
    public ResponseEntity<GenericResultResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(loginService.logout(request, response));
    }

    @PostMapping("/restore")
    public ResponseEntity<GenericResultResponse> restorePassword(@RequestBody PasswordRestoreRequest request) {
        return ResponseEntity.ok(passwordRestoreService.restorePassword(request));
    }

    @PostMapping("/password")
    public ResponseEntity<GenericResultResponse> changePassword(@RequestBody PasswordChangeRequest request) {
        return ResponseEntity.ok(passwordChangeService.changePassword(request));
    }
}
