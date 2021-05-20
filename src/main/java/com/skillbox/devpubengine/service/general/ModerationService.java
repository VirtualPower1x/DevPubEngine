package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.request.general.ModerationRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModerationService {

    private final PostRepository postRepository;

    public ModerationService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public GenericResultResponse moderate (ModerationRequest request) {
        Optional<PostEntity> postOptional = postRepository.findById(request.getPostId());
        if (postOptional.isEmpty()) {
            return new GenericResultResponse(false);
        }
        PostEntity post = postOptional.get();
        switch (request.getDecision()) {
            case "accept":
                post.setModerationStatus(ModerationStatus.ACCEPTED);
                break;
            case "decline":
                post.setModerationStatus(ModerationStatus.DECLINED);
                break;
        }
        postRepository.save(post);
        return new GenericResultResponse(true);
    }
}
