package com.skillbox.devpubengine.service.auth;

import com.skillbox.devpubengine.api.request.auth.RegisterRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.CaptchaCodeRepository;
import com.skillbox.devpubengine.repository.UserRepository;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService {

    private final CaptchaCodeRepository captchaCodeRepository;
    private final UserRepository userRepository;

    public RegisterService(CaptchaCodeRepository captchaCodeRepository, UserRepository userRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GenericResultResponse register (RegisterRequest request) {
        Map<String, String> errors = verifyUserData(request);
        if (!errors.isEmpty()) {
            return new GenericResultResponse(false, errors);
        }

        LocalDateTime currentTimeUtc = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
        String passwordHash = encoder.encode(request.getPassword());
        UserEntity user = new UserEntity(
                false,
                currentTimeUtc,
                request.getName(),
                request.getEmail(),
                passwordHash);
        userRepository.save(user);
        return new GenericResultResponse(true);
    }

    private Map<String, String> verifyUserData (RegisterRequest request) {
        Map<String, String> errors = new HashMap<>();
        final String STR_EMAIL_IN_USE = "Этот e-mail уже зарегистрирован";
        final String STR_INCORRECT_NAME = "Имя уже используется или введено некорректно";
        final String STR_SHORT_PW = "Пароль короче 6-ти символов";
        final String STR_BAD_CAPTCHA = "Код с картинки введён неверно";
        String nameRegExp = "[a-zA-Z0-9\\[\\]()!?+-_@$%&*.,№^|]{3}[a-zA-Z0-9\\[\\]()!?+-_@$%&*.,№^|]*";

        if (userRepository.existsByEmail(request.getEmail())) {
            errors.put("email", STR_EMAIL_IN_USE);
        }
        if (!request.getName().matches(nameRegExp) ||
                userRepository.existsByName(request.getName())) {
            errors.put("name", STR_INCORRECT_NAME);
        }
        if (request.getPassword().length() < 6) {
            errors.put("password", STR_SHORT_PW);
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
