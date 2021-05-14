package com.skillbox.devpubengine.utils.mapper;

import com.skillbox.devpubengine.api.response.auth.LoginUserData;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    PostRepository postRepository;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "photo", target = "photo")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "isModerator", target = "moderation")
    @Mapping(source = "isModerator", target = "moderationCount", qualifiedByName = "getModerationCount")
    @Mapping(source = "isModerator", target = "settings")
    public abstract LoginUserData userEntityToLoginUserData(UserEntity userEntity);

    @Named("getModerationCount")
    public int getModerationCount(boolean isModerator) {
        if (isModerator) {
            return postRepository.countAllByModerationStatus(ModerationStatus.NEW);
        }
        return 0;
    }
}
