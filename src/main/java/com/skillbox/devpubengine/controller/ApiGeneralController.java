package com.skillbox.devpubengine.controller;

import com.skillbox.devpubengine.api.request.general.*;
import com.skillbox.devpubengine.api.response.general.*;
import com.skillbox.devpubengine.service.general.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagsService tagsService;
    private final CalendarService calendarService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final EditProfileService editProfileService;
    private final ModerationService moderationService;
    private final StatisticsService statisticsService;

    public ApiGeneralController(InitResponse initResponse,
                                SettingsService settingsService,
                                TagsService tagsService,
                                CalendarService calendarService,
                                CommentService commentService,
                                ImageService imageService,
                                EditProfileService editProfileService,
                                ModerationService moderationService,
                                StatisticsService statisticsService) {
        this.initResponse = initResponse;
        this.settingsService = settingsService;
        this.tagsService = tagsService;
        this.calendarService = calendarService;
        this.commentService = commentService;
        this.imageService = imageService;
        this.editProfileService = editProfileService;
        this.moderationService = moderationService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/init")
    public ResponseEntity<InitResponse> init () {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> getSettings () {
        return ResponseEntity.ok(settingsService.getSettings());
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponse> getTags (String query) {
        return ResponseEntity.ok(tagsService.getTags(query));
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> getCalendar (Integer year) {
        return ResponseEntity.ok(calendarService.getCalendar(year));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/comment")
    public ResponseEntity<AddCommentResponse> addComment (@RequestBody AddCommentRequest request) {
        AddCommentResponse response = commentService.addComment(request);
        if (!response.isResult()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/image",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image) {
        return ResponseEntity.ok(imageService.uploadImage(image));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/profile/my",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GenericResultResponse> editUserProfile(@RequestBody EditProfileRequest request) {
        return ResponseEntity.ok(editProfileService.editProfile(request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping(value = "/profile/my",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GenericResultResponse> editUserProfileWithPhoto(@ModelAttribute EditProfileWithPhotoRequest request) {
        return ResponseEntity.ok(editProfileService.editProfileWithPhoto(request));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/statistics/my")
    public ResponseEntity<StatisticsResponse> getUserStatistics () {
        return ResponseEntity.ok(statisticsService.getUserStatistics());
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticsResponse> getAllStatistics () {
        return ResponseEntity.ok(statisticsService.getAllStatistics());
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PostMapping("/moderation")
    public ResponseEntity<GenericResultResponse> moderate (@RequestBody ModerationRequest request) {
        return ResponseEntity.ok(moderationService.moderate(request));
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @PutMapping("/settings")
    public ResponseEntity<Boolean> saveSettings (@RequestBody SettingsRequest request) {
        return ResponseEntity.ok(settingsService.saveSettings(request));
    }
}