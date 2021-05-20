package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetMyPostsRequest;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
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
public class GetUserPostsService {

    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;
    private final PostMapper postMapper;

    public GetUserPostsService(PostRepository postRepository,
                               CurrentUserService currentUserService,
                               PostMapper postMapper) {
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public PostsResponse getMyPosts (GetMyPostsRequest request) {
        if (postRepository.count() == 0) {
            return new PostsResponse();
        }
        int pageCount = request.getOffset() / request.getLimit();
        Page<PostEntity> page;
        Pageable pageable = PageRequest.of(pageCount, request.getLimit(), Sort.by("time").descending());
        UserEntity currentUser = currentUserService.getCurrentUser().orElseThrow(UnauthorizedException::new);
        switch (request.getStatus()) {
            case "inactive":
                page = postRepository
                        .findAllByUserAndIsActive(
                                pageable,
                                currentUser,
                                false);
                break;
            case "pending":
                page = postRepository
                        .findAllByUserAndIsActiveAndModerationStatus(
                                pageable,
                                currentUser,
                                true,
                                ModerationStatus.NEW);
                break;
            case "declined":
                page = postRepository
                        .findAllByUserAndIsActiveAndModerationStatus(
                                pageable,
                                currentUser,
                                true,
                                ModerationStatus.DECLINED);
                break;
            case "published":
            default:
                page = postRepository
                        .findAllByUserAndIsActiveAndModerationStatus(
                                pageable,
                                currentUser,
                                true,
                                ModerationStatus.ACCEPTED);
                break;
        }
        List<PostData> posts = page
                .stream()
                .map(postMapper::postEntityToPostData)
                .collect(Collectors.toList());
        return new PostsResponse((int) page.getTotalElements(), posts);
    }
}
