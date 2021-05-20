package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.LikeDislikeRequest;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.PostVoteEntity;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.repository.PostVoteRepository;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class LikeDislikePostService {

    private final PostVoteRepository postVoteRepository;
    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;

    public LikeDislikePostService(PostVoteRepository postVoteRepository,
                                  PostRepository postRepository,
                                  CurrentUserService currentUserService) {
        this.postVoteRepository = postVoteRepository;
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public GenericResultResponse likeDislikePost(LikeDislikeRequest request, byte value) {
        if (postRepository.findById(request.getPostId()).isEmpty()) {
            return new GenericResultResponse(false);
        }
        UserEntity currentUser = currentUserService.getCurrentUser().orElseThrow(UnauthorizedException::new);
        PostEntity post = postRepository.findById(request.getPostId()).get();
        PostVoteEntity vote;
        if (postVoteRepository.findByUserAndPost(currentUser, post).isEmpty()) {
            vote = new PostVoteEntity(
                    currentUser,
                    post,
                    LocalDateTime.now(ZoneOffset.UTC),
                    value);
        }
        else {
            vote = postVoteRepository.findByUserAndPost(currentUser, post).get();
            if (vote.getValue() == value) {
                return new GenericResultResponse(false);
            }
            vote.setTime(LocalDateTime.now(ZoneOffset.UTC));
            vote.setValue(value);
        }
        postVoteRepository.save(vote);
        return new GenericResultResponse(true);
    }
}
