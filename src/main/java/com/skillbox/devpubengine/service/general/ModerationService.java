package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.request.general.ModerationRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {

    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;

    public ModerationService(PostRepository postRepository,
                             CurrentUserService currentUserService) {
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
    }

    public GenericResultResponse moderate (ModerationRequest request) {
        UserEntity currentUser = currentUserService
                .getCurrentUser()
                .orElseThrow(UnauthorizedException::new);
        if (postRepository.findById(request.getPostId()).isEmpty()) {
            return new GenericResultResponse(false);
        }
        PostEntity post = postRepository.findById(request.getPostId()).get();
        switch (request.getDecision()) {
            case "accept":
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                break;
            case "decline":
                post.setModerationStatus(ModerationStatus.DECLINED);
                break;
        }
        post.setModeratorId(currentUser.getId());
        postRepository.save(post);
        return new GenericResultResponse(true);
    }
}
