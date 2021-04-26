package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.response.general.InitResponse;
import com.skillbox.devpubengine.api.response.general.SettingsResponse;
import com.skillbox.devpubengine.api.response.general.TagsResponse;
import com.skillbox.devpubengine.service.general.SettingsService;
import com.skillbox.devpubengine.service.general.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagsService tagsService;

    public ApiGeneralController(InitResponse initResponse,
                                SettingsService settingsService,
                                TagsService tagsService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagsService = tagsService;
    }

    @GetMapping("/init")
    public ResponseEntity<InitResponse> init () {
        return new ResponseEntity<>(initResponse, HttpStatus.OK);
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> getSettings () {
        return new ResponseEntity<>(settingsService.getSettings(), HttpStatus.OK);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponse> getTags (String query) {
        return new ResponseEntity<>(tagsService.getTags(query), HttpStatus.OK);
    }

}
