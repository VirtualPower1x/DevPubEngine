package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.service.auth.CurrentUserService;
import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GetPostByIDService {

    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;
    private final PostMapper postMapper;

    public GetPostByIDService(PostRepository postRepository,
                              CurrentUserService currentUserService,
                              PostMapper postMapper) {
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
        this.postMapper = postMapper;
    }

    @Transactional
    public PostByIDResponse getPostByID (int id) {
        Optional<PostEntity> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            PostEntity post = postOptional.get();
            if (currentUserService.getCurrentUser().isEmpty() ||
                    !(currentUserService.getCurrentUser().get().equals(post.getUser()) ||
                            currentUserService.getCurrentUser().get().getIsModerator())) {
                post.setViewCount(post.getViewCount() + 1);
            }
            postRepository.save(post);
            return postMapper.postEntityToPostByIDResponse(post);
        }
        return null;
    }
}