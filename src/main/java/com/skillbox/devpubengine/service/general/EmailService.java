package com.skillbox.devpubengine.service.general;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRestoreLink (String name, String to, String code) {
        final String BASE_URL = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
        String link = BASE_URL + "/login/change-password/" + code;
        final String STR_SUBJ = "Восстановление пароля к аккаунту devpub";
        final String STR_TEXT = String.format(
                "Здравствуйте, %s!" + System.lineSeparator() +
                        "Вы получили это сообщение, так как данный адрес электронной почты был указан " +
                        "при регистрации в блоге DevPub." + System.lineSeparator() +
                        "Ваша ссылка для восстановления пароля: %s " + System.lineSeparator() +
                        "Если вы не запрашивали восстановление пароля, проигнорируйте данное сообщение.",
                name, link);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@devpub.com");
        message.setTo(to);
        message.setSubject(STR_SUBJ);
        message.setText(STR_TEXT);
        mailSender.send(message);
    }
}
