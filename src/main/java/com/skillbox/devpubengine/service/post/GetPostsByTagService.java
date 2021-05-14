package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsByTagRequest;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsByTagService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public GetPostsByTagService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public PostsResponse getPostsByTag (GetPostsByTagRequest request) {
        if (request.getTag().equals("")) {
            return new PostsResponse();
        }
        int pageCount = request.getOffset() / request.getLimit();
        Pageable pageable = PageRequest.of(pageCount, request.getLimit(), Sort.by("time").descending());
        Page<PostEntity> page = postRepository.findActivePostsByTag(pageable, request.getTag());
        List<PostData> posts = page
                .stream()
                .map(postMapper::postEntityToPostData)
                .collect(Collectors.toList());
        return new PostsResponse((int) page.getTotalElements(), posts);
    }
}
