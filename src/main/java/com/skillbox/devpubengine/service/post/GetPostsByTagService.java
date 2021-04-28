package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsByTagRequest;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsByTagService extends PostsService{
    public GetPostsByTagService(PostRepository postRepository) {
        super(postRepository);
    }

    public PostsResponse getPostsByTag (GetPostsByTagRequest request) {
        if (request.getTag().equals("")) {
            return new PostsResponse();
        }
        List<PostData> posts = getPostRepository()
                .findAllActivePosts(Sort.by("time").descending())
                .stream()
                .filter(e -> this.hasTag(e, request.getTag()))
                .map(this::mapPostData)
                .collect(Collectors.toList());
        if (posts.size() == 0) {
            return new PostsResponse();
        }
        return completeResponse(request.getOffset(), request.getLimit(), posts);
    }

    private boolean hasTag (PostEntity post, String tagName) {
        return post.getTag2PostEntities()
                .stream()
                .anyMatch(e -> e.getTag2PostID().getTag().getName().equals(tagName));
    }
}
