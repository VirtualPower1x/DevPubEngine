package com.skillbox.devpubengine.utils.mapper;

import com.skillbox.devpubengine.api.response.post.*;
import com.skillbox.devpubengine.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "time", target = "timestamp")
    @Mapping(source = "user", target = "user", qualifiedByName = "user")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "text", target = "announce", qualifiedByName = "announce")
    @Mapping(source = "postVoteEntities", target = "likeCount", qualifiedByName = "likeCount")
    @Mapping(source = "postVoteEntities", target = "dislikeCount", qualifiedByName = "dislikeCount")
    @Mapping(expression = "java(post.getPostCommentEntities().size())", target = "commentCount")
    @Mapping(source = "viewCount", target = "viewCount")
    PostData postEntityToPostData (PostEntity post);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "time", target = "timestamp")
    @Mapping(source = "isActive", target = "active")
    @Mapping(source = "user", target = "user", qualifiedByName = "user")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "postVoteEntities", target = "likeCount", qualifiedByName = "likeCount")
    @Mapping(source = "postVoteEntities", target = "dislikeCount", qualifiedByName = "dislikeCount")
    @Mapping(source = "viewCount", target = "viewCount")
    @Mapping(source = "postCommentEntities", target = "comments")
    @Mapping(source = "tag2PostEntities", target = "tags")
    PostByIDResponse postEntityToPostByIDResponse (PostEntity post);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "time", target = "timestamp")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "user", target = "user", qualifiedByName = "userWithPhoto")
    CommentData postCommentEntityToCommentData (PostCommentEntity comment);

    default long timeToTimestamp (LocalDateTime ldt) {
        return ldt.atOffset(ZoneOffset.UTC).toEpochSecond();
    }

    @Named("user")
    default UserData userDataFromEntity(UserEntity user) {
        return new UserData(user.getId(), user.getName());
    }

    @Named("userWithPhoto")
    default UserWithPhotoData userWithPhotoDataFromEntity(UserEntity user) {
        return new UserWithPhotoData(user.getId(), user.getName(), user.getPhoto());
    }

    @Named("announce")
    default String announceFromText(String text) {
        String textWithoutTags = text.replaceAll("<.*?>", "");
        return textWithoutTags.length() > 150 ?
                textWithoutTags.substring(0, 149) + "..." :
                textWithoutTags;
    }

    @Named("likeCount")
    default int likeCountFromVotes (List<PostVoteEntity> votes) {
        return (int) votes
                .stream()
                .filter(e -> e.getValue() == 1)
                .count();
    }

    @Named("dislikeCount")
    default int dislikeCountFromVotes (List<PostVoteEntity> votes) {
        return (int) votes
                .stream()
                .filter(e -> e.getValue() == -1)
                .count();
    }

    default String tagNamesFromTag2PostList (Tag2PostEntity tag2Post) {
        return tag2Post.getTag2PostID().getTag().getName();
    }
}
