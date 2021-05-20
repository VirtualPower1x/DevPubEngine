package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.post.*;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.service.post.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final GetPostsService getPostsService;
    private final SearchPostsService searchPostsService;
    private final GetPostsByDateService getPostsByDateService;
    private final GetPostsByTagService getPostsByTagService;
    private final GetPostByIDService getPostByIDService;
    private final GetMyPostsService getMyPostsService;
    private final GetPostsForModerationService getPostsForModerationService;
    private final AddUpdatePostService addUpdatePostService;
    private final LikeDislikePostService likeDislikePostService;

    public ApiPostController(GetPostsService getPostsService,
                             SearchPostsService searchPostsService,
                             GetPostsByDateService getPostsByDateService,
                             GetPostsByTagService getPostsByTagService,
                             GetPostByIDService getPostByIDService,
                             GetMyPostsService getMyPostsService,
                             GetPostsForModerationService getPostsForModerationService,
                             AddUpdatePostService addUpdatePostService,
                             LikeDislikePostService likeDislikePostService) {
        this.getPostsService = getPostsService;
        this.searchPostsService = searchPostsService;
        this.getPostsByDateService = getPostsByDateService;
        this.getPostsByTagService = getPostsByTagService;
        this.getPostByIDService = getPostByIDService;
        this.getMyPostsService = getMyPostsService;
        this.getPostsForModerationService = getPostsForModerationService;
        this.addUpdatePostService = addUpdatePostService;
        this.likeDislikePostService = likeDislikePostService;
    }

    @GetMapping("")
    public ResponseEntity<PostsResponse> getPosts (GetPostsRequest getPostsRequest) {
        return ResponseEntity.ok(getPostsService.getPosts(getPostsRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> searchPosts (SearchPostsRequest searchPostsRequest) {
        return ResponseEntity.ok(searchPostsService.searchPosts(searchPostsRequest));
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate (GetPostsByDateRequest getPostsByDateRequest) {
        return ResponseEntity.ok(getPostsByDateService.getPostsByDate(getPostsByDateRequest));
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag (GetPostsByTagRequest getPostsByTagRequest) {
        return ResponseEntity.ok(getPostsByTagService.getPostsByTag(getPostsByTagRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostByIDResponse> getPostByID (@PathVariable int id) {
        PostByIDResponse response = getPostByIDService.getPostByID(id);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public ResponseEntity<PostsResponse> getMyPosts (GetMyPostsRequest request) {
        return ResponseEntity.ok(getMyPostsService.getMyPosts(request));
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping("/moderation")
    public ResponseEntity<PostsResponse> getPostsForModeration(GetPostsForModerationRequest request) {
        return ResponseEntity.ok(getPostsForModerationService.getPostsForModeration(request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("")
    public ResponseEntity<GenericResultResponse> addPost(@RequestBody AddPostRequest request){
        return ResponseEntity.ok(addUpdatePostService.addPost(request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/{id}")
    public ResponseEntity<GenericResultResponse> updatePost(@PathVariable int id, @RequestBody AddPostRequest request){
        return ResponseEntity.ok(addUpdatePostService.updatePost(id, request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/like")
    public ResponseEntity<GenericResultResponse> likePost(@RequestBody LikeDislikeRequest request){
        return ResponseEntity.ok(likeDislikePostService.likeDislikePost(request, (byte) 1));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/dislike")
    public ResponseEntity<GenericResultResponse> dislikePost(@RequestBody LikeDislikeRequest request){
        return ResponseEntity.ok(likeDislikePostService.likeDislikePost(request, (byte) -1));
    }
}