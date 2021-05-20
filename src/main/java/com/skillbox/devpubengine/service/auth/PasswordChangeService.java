package com.skillbox.devpubengine.service.auth;

import com.skillbox.devpubengine.api.request.auth.PasswordChangeRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.CaptchaCodeRepository;
import com.skillbox.devpubengine.repository.UserRepository;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordChangeService {

    private final UserRepository userRepository;
    private final CaptchaCodeRepository captchaCodeRepository;

    public PasswordChangeService(UserRepository userRepository,
                                 CaptchaCodeRepository captchaCodeRepository) {
        this.userRepository = userRepository;
        this.captchaCodeRepository = captchaCodeRepository;
    }

    @Transactional
    public GenericResultResponse changePassword (PasswordChangeRequest request) {
        Map<String, String> errors = verifyPasswordData(request);
        if (!errors.isEmpty() || userRepository.findByCode(request.getCode()).isEmpty()) {
            return new GenericResultResponse(false, errors);
        }
        UserEntity user = userRepository.findByCode(request.getCode()).get();
        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCode(null);
        userRepository.save(user);
        return new GenericResultResponse(true);
    }

    private Map<String, String> verifyPasswordData (PasswordChangeRequest request) {
        Map<String, String> errors = new HashMap<>();
        final String STR_CODE_OUTDATED = "Ссылка для восстановления пароля устарела.\n" +
                "<a href=\"/auth/restore\">Запросить ссылку снова</a>";
        final String STR_SHORT_PW = "Пароль короче 6-ти символов";
        final String STR_BAD_CAPTCHA = "Код с картинки введён неверно";

        if (request.getPassword().length() < 6) {
            errors.put("password", STR_SHORT_PW);
        }
        if (!userRepository.existsByCode(request.getCode())) {
            errors.put("code", STR_CODE_OUTDATED);
        }
        if (captchaCodeRepository
                .findBySecretCode(request.getCaptchaSecret())
                .isEmpty() ||
                !captchaCodeRepository
                        .findBySecretCode(request.getCaptchaSecret())
                        .get()
                        .getCode()
                        .equals(request.getCaptcha())) {
            errors.put("captcha", STR_BAD_CAPTCHA);
        }
        return errors;
    }
}
