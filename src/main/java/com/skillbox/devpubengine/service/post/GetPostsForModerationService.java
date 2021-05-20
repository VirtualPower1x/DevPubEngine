package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsForModerationRequest;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsForModerationService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CurrentUserService currentUserService;

    public GetPostsForModerationService(PostRepository postRepository,
                                        PostMapper postMapper,
                                        CurrentUserService currentUserService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.currentUserService = currentUserService;
    }

    public PostsResponse getPostsForModeration(GetPostsForModerationRequest request) {
        if (postRepository.count() == 0) {
            return new PostsResponse();
        }
        int moderatorId = currentUserService
                .getCurrentUser()
                .orElseThrow(UnauthorizedException::new)
                .getId();
        int pageCount = request.getOffset() / request.getLimit();
        Page<PostEntity> page;
        Pageable pageable = PageRequest.of(pageCount, request.getLimit(), Sort.by("time").descending());
        switch (request.getStatus()) {
            case "declined":
                page = postRepository
                        .findAllByIsActiveIsTrueAndModerationStatusAndModeratorId(
                                pageable,
                                ModerationStatus.DECLINED,
                                moderatorId);
                break;
            case "accepted":
                page = postRepository
                        .findAllByIsActiveIsTrueAndModerationStatusAndModeratorId(
                                pageable,
                                ModerationStatus.ACCEPTED,
                                moderatorId);
                break;
            case "new":
            default:
                page = postRepository
                        .findAllByIsActiveIsTrueAndModerationStatus(
                                pageable,
                                ModerationStatus.NEW);
                break;
        }
        List<PostData> posts = page
                .stream()
                .map(postMapper::postEntityToPostData)
                .collect(Collectors.toList());
        return new PostsResponse((int) page.getTotalElements(), posts);
    }
}
