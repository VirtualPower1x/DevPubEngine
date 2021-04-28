package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.SearchPostsRequest;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchPostsService extends PostsService {
    public SearchPostsService(PostRepository postRepository) {
        super(postRepository);
    }

    public PostsResponse searchPosts (SearchPostsRequest request) {
        List<PostData> posts = getPostRepository()
                .findAllActivePosts(Sort.by("time").descending())
                .stream()
                .filter(e -> e.getTitle().contains(request.getQuery().trim()) ||
                        e.getText().contains(request.getQuery().trim()))
                .map(super::mapPostData)
                .collect(Collectors.toList());
        if (posts.size() == 0) {
            return new PostsResponse();
        }
        return completeResponse(request.getOffset(), request.getLimit(), posts);
    }
}