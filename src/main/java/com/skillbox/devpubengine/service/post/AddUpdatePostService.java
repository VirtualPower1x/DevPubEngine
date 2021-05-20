package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.AddPostRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.model.*;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.repository.Tag2PostRepository;
import com.skillbox.devpubengine.repository.TagRepository;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import com.skillbox.devpubengine.service.general.SettingsService;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AddUpdatePostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final Tag2PostRepository tag2PostRepository;
    private final PostMapper postMapper;
    private final CurrentUserService currentUserService;
    private final SettingsService settingsService;

    public AddUpdatePostService(PostRepository postRepository,
                                TagRepository tagRepository,
                                Tag2PostRepository tag2PostRepository,
                                PostMapper postMapper,
                                CurrentUserService currentUserService,
                                SettingsService settingsService) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.tag2PostRepository = tag2PostRepository;
        this.postMapper = postMapper;
        this.currentUserService = currentUserService;
        this.settingsService = settingsService;
    }

    @Transactional
    public GenericResultResponse addPost(AddPostRequest request) {
        Map<String, String> errors = verifyPost(request);
        if (!errors.isEmpty()) {
            return new GenericResultResponse(false, errors);
        }
        UserEntity currentUser = currentUserService.getCurrentUser().orElseThrow(UnauthorizedException::new);
        PostEntity newPost = postRepository.save(postMapper.addPostRequestToPostEntity(
                request, getDefaultModerationStatus(currentUser), currentUser));
        applyTags(newPost, request.getTags());
        return new GenericResultResponse(true);
    }

    @Transactional
    public GenericResultResponse updatePost(int id, AddPostRequest request) {
        Map<String, String> errors = verifyPost(request);
        if (!errors.isEmpty()) {
            return new GenericResultResponse(false, errors);
        }
        UserEntity currentUser = currentUserService.getCurrentUser().orElseThrow(UnauthorizedException::new);
        PostEntity newPost = postMapper.addPostRequestToPostEntity(
                request, getDefaultModerationStatus(currentUser), currentUser);
        newPost.setId(id);
        newPost = postRepository.save(newPost);
        deleteOldTags(newPost);
        applyTags(newPost, request.getTags());
        return new GenericResultResponse(true);
    }

    private Map<String, String> verifyPost (AddPostRequest request) {
        Map<String, String> errors = new HashMap<>();
        final String STR_TOO_SHORT_TITLE = "Заголовок не установлен";
        final String STR_TOO_SHORT_TEXT = "Текст публикации слишком короткий";
        if (request.getTitle().length() < 3) {
            errors.put("title", STR_TOO_SHORT_TITLE);
        }
        if (request.getText().length() < 50) {
            errors.put("text", STR_TOO_SHORT_TEXT);
        }
        return errors;
    }

    @Transactional
    protected void applyTags (PostEntity post, List<String> tags) {
        for (String tagName : tags) {
            final String tagNameFinal = tagName.toLowerCase(Locale.ROOT);
            TagEntity tag = tagRepository
                    .findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new TagEntity(tagNameFinal)));
            Tag2PostEntity tag2Post = new Tag2PostEntity(new Tag2PostID(post, tag));
            tag2PostRepository.save(tag2Post);
        }
    }

    @Transactional
    protected void deleteOldTags (PostEntity post) {
        tag2PostRepository.deleteInBatch(tag2PostRepository.findAllByPost(post));
    }

    @Transactional
    protected ModerationStatus getDefaultModerationStatus(UserEntity currentUser) {
        if (settingsService.getSettings().isPostPremoderation() && !currentUser.getIsModerator()) {
            return ModerationStatus.NEW;
        }
        return ModerationStatus.ACCEPTED;
    }
}
