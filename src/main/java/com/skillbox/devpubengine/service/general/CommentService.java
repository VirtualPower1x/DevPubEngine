package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.request.general.AddCommentRequest;
import com.skillbox.devpubengine.api.response.general.AddCommentResponse;
import com.skillbox.devpubengine.exception.NotFoundException;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.model.PostCommentEntity;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.PostCommentRepository;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import com.skillbox.devpubengine.utils.mapper.PostCommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final CurrentUserService currentUserService;
    private final PostCommentMapper postCommentMapper;

    public CommentService(PostRepository postRepository,
                          PostCommentRepository postCommentRepository,
                          CurrentUserService currentUserService,
                          PostCommentMapper postCommentMapper) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.currentUserService = currentUserService;
        this.postCommentMapper = postCommentMapper;
    }

    @Transactional
    public AddCommentResponse addComment (AddCommentRequest request) {
        UserEntity currentUser = currentUserService
                .getCurrentUser()
                .orElseThrow(UnauthorizedException::new);
        PostEntity post = postRepository
                .findById(request.getPostId())
                .orElseThrow(() -> new NotFoundException(PostEntity.class, "id",
                        String.valueOf(request.getPostId())));
        if (request.getParentId() != null && postCommentRepository.findById(request.getParentId()).isEmpty()) {
            throw new NotFoundException(PostCommentEntity.class, "id",
                            String.valueOf(request.getParentId()));
        }
        Map<String, String> errors = verifyComment(request);
        if (!errors.isEmpty()) {
            return new AddCommentResponse(false, errors);
        }
        return new AddCommentResponse( true,
                postCommentRepository.save(
                        postCommentMapper
                                .addCommentRequestToPostCommentEntity(request, currentUser, post))
                        .getId());
    }

    private Map<String, String> verifyComment (AddCommentRequest request) {
        Map<String, String> errors = new HashMap<>();
        final String STR_TOO_SHORT_TEXT = "Текст комментария не задан или слишком короткий";
        if (request.getText().length() < 3) {
            errors.put("text", STR_TOO_SHORT_TEXT);
        }
        return errors;
    }
}
