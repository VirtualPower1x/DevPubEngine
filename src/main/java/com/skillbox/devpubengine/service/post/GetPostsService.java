package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsRequest;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsService extends PostsService{
    public GetPostsService(PostRepository postRepository) {
        super(postRepository);
    }

    public PostsResponse getPosts (GetPostsRequest request) {
        calculatePostsCount();
        if (getPostsCount() == 0) {
            return new PostsResponse();
        }
        Sort sort;
        switch (request.getMode()) {
            case "popular":
                sort = JpaSort.unsafe(Sort.Direction.DESC, "size(postCommentEntities)");
                break;
            case "best":
                //this doesn't work
                //sort = JpaSort.unsafe(Sort.Direction.DESC, "(SELECT SUM(v.value) FROM PostEntity.postVoteEntities v WHERE v.value = 1 GROUP BY v.value)");
                List<PostData> posts = getPostRepository()
                        .findAllSortedByLikesCount()
                        .stream()
                        .map(this::mapPostData)
                        .collect(Collectors.toList());
                return completeResponse(request.getOffset(), request.getLimit(), posts);
            case "early":
                sort = Sort.by("time");
                break;
            case "recent":
            default:
                sort = Sort.by("time").descending();
                break;
        }
        List<PostData> posts = getPostRepository()
                .findAllActivePosts(sort)
                .stream()
                .map(this::mapPostData)
                .collect(Collectors.toList());
        return completeResponse(request.getOffset(), request.getLimit(), posts);
    }
}