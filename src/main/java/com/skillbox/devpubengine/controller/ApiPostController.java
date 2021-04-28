package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.post.GetPostsByDateRequest;
import com.skillbox.devpubengine.api.request.post.GetPostsByTagRequest;
import com.skillbox.devpubengine.api.request.post.GetPostsRequest;
import com.skillbox.devpubengine.api.request.post.SearchPostsRequest;
import com.skillbox.devpubengine.api.response.post.PostByIDResponse;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.service.post.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final GetPostsService getPostsService;
    private final SearchPostsService searchPostsService;
    private final GetPostsByDateService getPostsByDateService;
    private final GetPostsByTagService getPostsByTagService;
    private final GetPostByIDService getPostByIDService;

    public ApiPostController(GetPostsService getPostsService,
                             SearchPostsService searchPostsService,
                             GetPostsByDateService getPostsByDateService,
                             GetPostsByTagService getPostsByTagService,
                             GetPostByIDService getPostByIDService) {
        this.getPostsService = getPostsService;
        this.searchPostsService = searchPostsService;
        this.getPostsByDateService = getPostsByDateService;
        this.getPostsByTagService = getPostsByTagService;
        this.getPostByIDService = getPostByIDService;
    }

    @GetMapping("")
    public ResponseEntity<PostsResponse> getPosts (GetPostsRequest getPostsRequest) {
        return new ResponseEntity<>(getPostsService.getPosts(getPostsRequest), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PostsResponse> searchPosts (SearchPostsRequest searchPostsRequest) {
        return new ResponseEntity<>(searchPostsService.searchPosts(searchPostsRequest), HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostsResponse> getPostsByDate (GetPostsByDateRequest getPostsByDateRequest) {
        return new ResponseEntity<>(getPostsByDateService.getPostsByDate(getPostsByDateRequest),
                HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostsResponse> getPostsByTag (GetPostsByTagRequest getPostsByTagRequest) {
        return new ResponseEntity<>(getPostsByTagService.getPostsByTag(getPostsByTagRequest),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostByIDResponse> getPostByID (@PathVariable int id) {
        PostByIDResponse response = getPostByIDService.getPostByID(id);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}