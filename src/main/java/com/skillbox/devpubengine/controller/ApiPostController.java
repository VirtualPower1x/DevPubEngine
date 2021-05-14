package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.post.*;
import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.service.post.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final GetPostsService getPostsService;
    private final SearchPostsService searchPostsService;
    private final GetPostsByDateService getPostsByDateService;
    private final GetPostsByTagService getPostsByTagService;
    private final GetPostByIDService getPostByIDService;
    private final GetMyPostsService getMyPostsService;

    public ApiPostController(GetPostsService getPostsService,
                             SearchPostsService searchPostsService,
                             GetPostsByDateService getPostsByDateService,
                             GetPostsByTagService getPostsByTagService,
                             GetPostByIDService getPostByIDService,
                             GetMyPostsService getMyPostsService) {
        this.getPostsService = getPostsService;
        this.searchPostsService = searchPostsService;
        this.getPostsByDateService = getPostsByDateService;
        this.getPostsByTagService = getPostsByTagService;
        this.getPostByIDService = getPostByIDService;
        this.getMyPostsService = getMyPostsService;
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
    public ResponseEntity<PostByIDResponse> getPostByID (@PathVariable int id, Principal principal) {
        PostByIDResponse response = getPostByIDService.getPostByID(id, principal);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public ResponseEntity<PostsResponse> getMyPosts (GetMyPostsRequest request, Principal principal) {
        return ResponseEntity.ok(getMyPostsService.getMyPosts(request, principal));
    }
}