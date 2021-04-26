package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsRequest;
import com.skillbox.devpubengine.api.response.post.GetPostsResponse;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.UserData;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsService {

    private final PostRepository postRepository;

    private static volatile int postsCount = 0;

    public GetPostsService(PostRepository postRepository) {
        this.postRepository = postRepository;
        calculatePostsCount();
    }

    public static int getPostsCount() {
        return postsCount;
    }

    public GetPostsResponse getPosts (GetPostsRequest request) {
        GetPostsResponse response = new GetPostsResponse();
        calculatePostsCount();
        if (postsCount == 0) {
            return response;
        }

        List<PostData> posts = postRepository.findAll()
                .stream()
                .filter(e -> e.getIsActive() == 1 &&
                        e.getModerationStatus() == ModerationStatus.ACCEPTED &&
                        !e.getTime().isAfter(LocalDateTime.now()))
                .map(this::mapPostData)
                .collect(Collectors.toList());
        switch (request.getMode()) {
            case "recent":
                posts.sort((p1, p2) -> Long.compare(p2.getTimestamp(), p1.getTimestamp()));
                break;
            case "popular":
                posts.sort((p1, p2) -> Integer.compare(p2.getCommentCount(), p1.getCommentCount()));
                break;
            case "best":
                posts.sort((p1, p2) -> Integer.compare(p2.getLikeCount(), p1.getLikeCount()));
                break;
            case "early":
                posts.sort(Comparator.comparingLong(PostData::getTimestamp));
                break;
        }
        int range = Math.min(request.getOffset() + request.getLimit(), postsCount);
        List<PostData> postsToDisplay = new java.util.ArrayList<>(Collections.emptyList());
        for (int i = request.getOffset(); i < range; i++) {
            postsToDisplay.add(posts.get(i));
        }
        response.setCount(postsCount);
        response.setPosts(postsToDisplay);
        return response;
    }

    private synchronized void calculatePostsCount () {
        postsCount = (int) postRepository.findAll()
                .stream()
                .filter(e -> e.getIsActive() == 1 &&
                        e.getModerationStatus() == ModerationStatus.ACCEPTED &&
                        !e.getTime().isAfter(LocalDateTime.now()))
                .count();
    }

    private PostData mapPostData (PostEntity entity) {
        PostData data = new PostData();
        ZoneId zoneId = ZoneId.systemDefault();
        String postPreview = entity.getText().length() > 150 ?
                entity.getText().substring(0, 149) + "..." :
                entity.getText();
        int likeCount = (int) entity.getPostVoteEntities()
                .stream()
                .filter(e -> e.getValue() == 1)
                .count();
        int dislikeCount = (int) entity.getPostVoteEntities()
                .stream()
                .filter(e -> e.getValue() == -1)
                .count();
        int commentCount = entity.getPostCommentEntities().size();

        data.setId(entity.getId());
        data.setTimestamp(entity.getTime().atZone(zoneId).toEpochSecond());
        data.setUser(new UserData(entity.getUser().getId(), entity.getUser().getName()));
        data.setTitle(entity.getTitle());
        data.setAnnounce(postPreview);
        data.setLikeCount(likeCount);
        data.setDislikeCount(dislikeCount);
        data.setCommentCount(commentCount);
        data.setViewCount(entity.getViewCount());
        return data;
    }
}
