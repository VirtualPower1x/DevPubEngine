package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsByDateRequest;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsByDateService extends PostsService{
    public GetPostsByDateService(PostRepository postRepository) {
        super(postRepository);
    }

    public PostsResponse getPostsByDate (GetPostsByDateRequest request) {
        if (request.getDate().equals("")) {
            return new PostsResponse();
        }
        List<PostData> posts = getPostRepository()
                .findAllActivePosts(Sort.by("time").descending())
                .stream()
                .filter(e -> convertLocalDateTimeToString(e.getTime()).equals(request.getDate()))
                .map(this::mapPostData)
                .collect(Collectors.toList());
        if (posts.size() == 0) {
            return new PostsResponse();
        }
        return completeResponse(request.getOffset(), request.getLimit(), posts);
    }

    private String convertLocalDateTimeToString (LocalDateTime ldt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        return (sdf.format(Date.from(instant)));
    }
}
