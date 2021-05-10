package com.skillbox.devpubengine.service.auth;

import com.skillbox.devpubengine.api.request.auth.RegisterRequest;
import com.skillbox.devpubengine.api.response.auth.RegisterResponse;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.CaptchaCodeRepository;
import com.skillbox.devpubengine.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class RegisterService {

    private final CaptchaCodeRepository captchaCodeRepository;

    private final UserRepository userRepository;

    public RegisterService(CaptchaCodeRepository captchaCodeRepository, UserRepository userRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
        this.userRepository = userRepository;
    }

    public RegisterResponse register (RegisterRequest request) {
        Map<String, String> errors = verifyUserData(request);
        if (!errors.isEmpty()) {
            return new RegisterResponse(false, errors);
        }

        LocalDateTime currentTimeUtc = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        String passwordHash = toMD5Hash(request.getPassword());
        UserEntity user = new UserEntity(
                (byte) 0,
                currentTimeUtc,
                request.getName(),
                request.getEmail(),
                passwordHash);
        userRepository.save(user);
        return new RegisterResponse(true);
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

    private String toMD5Hash (String source) {
        String hash = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            hash = DatatypeConverter.printHexBinary(digest).toLowerCase(Locale.ROOT);
        }
        catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return hash;
    }
}
