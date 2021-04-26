package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.post.GetPostsRequest;
import com.skillbox.devpubengine.api.response.post.GetPostsResponse;
import com.skillbox.devpubengine.service.post.GetPostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final GetPostsService getPostsService;

    public ApiPostController(GetPostsService getPostsService) {
        this.getPostsService = getPostsService;
    }

    @GetMapping("")
    public ResponseEntity<GetPostsResponse> getPosts (GetPostsRequest getPostsRequest) {
        return new ResponseEntity<>(getPostsService.getPosts(getPostsRequest), HttpStatus.OK);
    }
}
