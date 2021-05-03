package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetPostByIDService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public GetPostByIDService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PostByIDResponse getPostByID (int id) {
        Optional<PostEntity> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            PostEntity post = postOptional.get();
            // TODO : check if authorized user is moderator or post author
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
            return postMapper.postEntityToPostByIDResponse(post);
        }
        return null;
    }
}