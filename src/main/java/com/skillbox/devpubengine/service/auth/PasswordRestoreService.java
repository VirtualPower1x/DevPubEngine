package com.skillbox.devpubengine.service.auth;

import com.skillbox.devpubengine.api.request.auth.PasswordRestoreRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.UserRepository;
import com.skillbox.devpubengine.service.general.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PasswordRestoreService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public PasswordRestoreService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public GenericResultResponse restorePassword (PasswordRestoreRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            return new GenericResultResponse(false);
        }
        UserEntity user = userRepository.findByEmail(request.getEmail()).get();
        String code = UUID.randomUUID().toString();
        user.setCode(code);
        userRepository.save(user);
        emailService.sendRestoreLink(user.getName(), user.getEmail(), code);
        return new GenericResultResponse(true);
    }
}
