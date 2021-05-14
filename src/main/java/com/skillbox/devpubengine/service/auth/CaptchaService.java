package com.skillbox.devpubengine.service.auth;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.skillbox.devpubengine.api.response.auth.CaptchaResponse;
import com.skillbox.devpubengine.model.CaptchaCodeEntity;
import com.skillbox.devpubengine.repository.CaptchaCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

@Service
public class CaptchaService {

    private final CaptchaCodeRepository captchaCodeRepository;

    @Value("${blog.config.captchaSavePeriod}")
    private long captchaSavePeriod;

    public CaptchaService(CaptchaCodeRepository captchaCodeRepository) {
        this.captchaCodeRepository = captchaCodeRepository;
    }

    @Transactional
    public CaptchaResponse getCaptcha() {
        Cage cage = new GCage();
        LocalDateTime currentTimeUtc = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
        String code = generateCaptchaCode();
        String secret = UUID.randomUUID().toString();
        String image = "data:image/png;base64, " + Base64.getEncoder().encodeToString(cage.draw(code));
        captchaCodeRepository.save(new CaptchaCodeEntity(currentTimeUtc, code, secret));
        return new CaptchaResponse(secret, image);
    }

    @Transactional
    public void deleteOutdatedCaptcha() {
        LocalDateTime dateTimeRange = LocalDateTime.now()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime()
                .minusSeconds(captchaSavePeriod);
        captchaCodeRepository.deleteInBatch(captchaCodeRepository.getAllByTimeBefore(dateTimeRange));
    }

    private String generateCaptchaCode () {
        final char[] symbols = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            result.append(symbols[(int) Math.round(Math.random() * (symbols.length - 1))]);
        }
        return result.toString();
    }
}
