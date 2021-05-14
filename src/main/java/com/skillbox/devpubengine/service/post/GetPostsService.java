package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsRequest;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public GetPostsService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public PostsResponse getPosts (GetPostsRequest request) {
        if (postRepository.count() == 0) {
            return new PostsResponse();
        }
        int pageCount = request.getOffset() / request.getLimit();
        Sort sort;
        switch (request.getMode()) {
            case "popular":
                sort = JpaSort.unsafe(Sort.Direction.DESC, "size(postCommentEntities)");
                break;
            case "best":
                Pageable pageable = PageRequest.of(pageCount, request.getLimit());
                Page<PostEntity> page = postRepository.findAllActivePostsOrderByLikesCount(pageable);
                List<PostData> posts = page
                        .stream()
                        .map(postMapper::postEntityToPostData)
                        .collect(Collectors.toList());
                return new PostsResponse((int) page.getTotalElements(), posts);
            case "early":
                sort = Sort.by("time");
                break;
            case "recent":
            default:
                sort = Sort.by("time").descending();
                break;
        }
        Pageable pageable = PageRequest.of(pageCount, request.getLimit(), sort);
        Page<PostEntity> page = postRepository.findAllActivePosts(pageable);
        List<PostData> posts = page
                .stream()
                .map(postMapper::postEntityToPostData)
                .collect(Collectors.toList());
        return new PostsResponse((int) page.getTotalElements(), posts);
    }
}