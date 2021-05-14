package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.repository.UserRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
public class GetPostByIDService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public GetPostByIDService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Transactional
    public PostByIDResponse getPostByID (int id, Principal principal) {
        Optional<PostEntity> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            PostEntity post = postOptional.get();
            if (principal == null) {
                post.setViewCount(post.getViewCount() + 1);
            }
            else {
                UserEntity currentUser = userRepository
                        .findByEmail(principal.getName())
                        .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("Cannot find login %s", principal.getName())));
                if (!currentUser.equals(post.getUser()) && !currentUser.getIsModerator()) {
                    post.setViewCount(post.getViewCount() + 1);
                }
            }
            postRepository.save(post);
            return postMapper.postEntityToPostByIDResponse(post);
        }
        return null;
    }
}