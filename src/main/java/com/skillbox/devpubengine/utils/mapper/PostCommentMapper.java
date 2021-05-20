package com.skillbox.devpubengine.utils.mapper;

import com.skillbox.devpubengine.api.request.general.AddCommentRequest;
import com.skillbox.devpubengine.model.PostCommentEntity;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Mapper(componentModel = "spring")
public interface PostCommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "request.parentId", target = "parentId")
    @Mapping(source = "post", target = "post")
    @Mapping(source = "currentUser", target = "user")
    @Mapping(expression = "java(getCurrentTime())", target = "time")
    @Mapping(source = "request.text", target = "text")
    PostCommentEntity addCommentRequestToPostCommentEntity (AddCommentRequest request, UserEntity currentUser, PostEntity post);

    default LocalDateTime getCurrentTime () {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
