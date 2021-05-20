package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.request.general.EditProfileRequest;
import com.skillbox.devpubengine.api.request.general.EditProfileWithPhotoRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.UserRepository;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import com.skillbox.devpubengine.utils.mapper.EditProfileMapper;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class EditProfileService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final PhotoService photoService;
    private final EditProfileMapper editProfileMapper;

    public EditProfileService(UserRepository userRepository,
                              CurrentUserService currentUserService,
                              PhotoService photoService,
                              EditProfileMapper editProfileMapper) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.photoService = photoService;
        this.editProfileMapper = editProfileMapper;
    }

    @Transactional
    public GenericResultResponse editProfile (EditProfileRequest request) {
        Map<String, String> errors = verifyProfileData(
                request.getEmail(),
                request.getName(),
                request.getPassword(),
                0);
        if (!errors.isEmpty()) {
            return new GenericResultResponse(false, errors);
        }
        updateUserData(request);
        return new GenericResultResponse(true);
    }

    @Transactional
    public GenericResultResponse editProfileWithPhoto (EditProfileWithPhotoRequest request) {
        Map<String, String> errors = verifyProfileData(
                request.getEmail(),
                request.getName(),
                request.getPassword(),
                request.getPhoto().getSize());
        if (!errors.isEmpty()) {
            return new GenericResultResponse(false, errors);
        }
        updateUserData(editProfileMapper.simplify(request, photoService.uploadPhoto(request.getPhoto())));
        return new GenericResultResponse(true);
    }

    @Transactional
    protected void updateUserData(EditProfileRequest request) {
        UserEntity currentUser = currentUserService.getCurrentUser().orElseThrow(UnauthorizedException::new);
        currentUser.setEmail(request.getEmail());
        currentUser.setName(request.getName());
        if (request.getPassword() != null) {
            SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
            currentUser.setPassword(encoder.encode(request.getPassword()));
        }
        if (request.getRemovePhoto() != null) {
            currentUser.setPhoto(request.getPhoto());
        }
        userRepository.save(currentUser);
    }

    private Map<String, String> verifyProfileData (String email, String name, String password, long photoSize) {
        Map<String, String> errors = new HashMap<>();
        final String STR_EMAIL_IN_USE = "Этот e-mail уже зарегистрирован";
        final String STR_TOO_LARGE_PHOTO = "Фото слишком большое, нужно не более 5 Мб";
        final String STR_INCORRECT_NAME = "Имя уже используется или введено некорректно";
        final String STR_SHORT_PW = "Пароль короче 6-ти символов";
        String nameRegExp = "[a-zA-Z0-9\\[\\]()!?+-_@$%&*.,№^|]{3}[a-zA-Z0-9\\[\\]()!?+-_@$%&*.,№^|]*";
        UserEntity currentUser = currentUserService.getCurrentUser().orElseThrow(UnauthorizedException::new);

        if (!currentUser.getEmail().equalsIgnoreCase(email) && userRepository.existsByEmail(email)) {
            errors.put("email", STR_EMAIL_IN_USE);
        }
        if (photoSize > 5 * 1024 * 1024) {
            errors.put("photo", STR_TOO_LARGE_PHOTO);
        }
        if (!name.matches(nameRegExp) ||
                (!currentUser.getName().equalsIgnoreCase(name) &&
                        userRepository.existsByName(name))) {
            errors.put("name", STR_INCORRECT_NAME);
        }
        if (password != null && password.length() < 6) {
            errors.put("password", STR_SHORT_PW);
        }
        return errors;
    }
}
