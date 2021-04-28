package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.response.post.CommentData;
import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.api.response.post.UserData;
import com.skillbox.devpubengine.api.response.post.UserWithPhotoData;
import com.skillbox.devpubengine.model.PostCommentEntity;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostByIDService extends PostsService{
    public GetPostByIDService(PostRepository postRepository) {
        super(postRepository);
    }

    public PostByIDResponse getPostByID (int id) {
        PostRepository repository = getPostRepository();
        var postOptional = repository.findById(id);
        if (postOptional.isPresent()) {
            PostEntity post = postOptional.get();
            // TODO : check if authorized user is moderator or post author
            post.setViewCount(post.getViewCount() + 1);
            repository.save(post);
            return mapPostResponse(post);
        }
        return null;
    }

    private PostByIDResponse mapPostResponse (PostEntity entity) {
        PostByIDResponse response = new PostByIDResponse();
        int likeCount = (int) entity.getPostVoteEntities()
                .stream()
                .filter(e -> e.getValue() == 1)
                .count();
        int dislikeCount = (int) entity.getPostVoteEntities()
                .stream()
                .filter(e -> e.getValue() == -1)
                .count();
        List<CommentData> comments = entity.getPostCommentEntities()
                .stream()
                .map(this::mapCommentData)
                .collect(Collectors.toList());
        List<String> tags = entity.getTag2PostEntities()
                .stream()
                .map(e -> e.getTag2PostID().getTag().getName())
                .collect(Collectors.toList());

        response.setId(entity.getId());
        response.setTimestamp(entity.getTime().atOffset(ZoneOffset.UTC).toEpochSecond());
        response.setActive(entity.getIsActive() == 1);
        response.setUser(new UserData(entity.getUser().getId(), entity.getUser().getName()));
        response.setTitle(entity.getTitle());
        response.setText(entity.getText());
        response.setLikeCount(likeCount);
        response.setDislikeCount(dislikeCount);
        response.setViewCount(entity.getViewCount());
        response.setComments(comments);
        response.setTags(tags);
        return response;
    }

    private CommentData mapCommentData (PostCommentEntity commentEntity) {
        CommentData data = new CommentData();
        UserWithPhotoData user = new UserWithPhotoData(
                commentEntity.getUser().getId(),
                commentEntity.getUser().getName(),
                commentEntity.getUser().getPhoto() == null ? "" : commentEntity.getUser().getPhoto());

        data.setId(commentEntity.getId());
        data.setTimestamp(commentEntity.getTime().atOffset(ZoneOffset.UTC).toEpochSecond());
        data.setText(commentEntity.getText());
        data.setUser(user);
        return data;
    }
}