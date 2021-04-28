package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.api.response.post.UserData;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@Service
public abstract class PostsService {

    private final PostRepository postRepository;
    private static volatile int postsCount = 0;

    public PostsService(PostRepository postRepository) {
        this.postRepository = postRepository;
        calculatePostsCount();
    }

    protected PostRepository getPostRepository() {
        return postRepository;
    }

    public static int getPostsCount() {
        return postsCount;
    }

    protected synchronized void calculatePostsCount () {
        postsCount = postRepository.findAllActivePosts().size();
    }

    protected PostData mapPostData (PostEntity entity) {
        PostData data = new PostData();
        String textNoTags = removeHtmlTags(entity.getText());
        String postPreview = textNoTags.length() > 150 ?
                textNoTags.substring(0, 149) + "..." :
                textNoTags;
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
        data.setTimestamp(entity.getTime().atOffset(ZoneOffset.UTC).toEpochSecond());
        data.setUser(new UserData(entity.getUser().getId(), entity.getUser().getName()));
        data.setTitle(entity.getTitle());
        data.setAnnounce(postPreview);
        data.setLikeCount(likeCount);
        data.setDislikeCount(dislikeCount);
        data.setCommentCount(commentCount);
        data.setViewCount(entity.getViewCount());
        return data;
    }

    private String removeHtmlTags (String source) {
        return source.replaceAll("<.*?>", "");
    }

    protected PostsResponse completeResponse(int offset, int limit, List<PostData> posts) {
        int range = Math.min(offset + limit, posts.size());
        PostsResponse response = new PostsResponse ();
        List<PostData> postsToDisplay = new java.util.ArrayList<> (Collections.emptyList());
        for (int i = offset; i < range; i++) {
            postsToDisplay.add(posts.get(i));
        }
        response.setCount(posts.size());
        response.setPosts(postsToDisplay);
        return response;
    }
}