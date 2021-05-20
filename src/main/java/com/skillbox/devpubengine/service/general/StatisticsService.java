package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.response.general.StatisticsResponse;
import com.skillbox.devpubengine.exception.UnauthorizedException;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.service.auth.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.util.List;

@Service
public class StatisticsService {

    private final PostRepository postRepository;
    private final CurrentUserService currentUserService;
    private final SettingsService settingsService;

    public StatisticsService(PostRepository postRepository,
                             CurrentUserService currentUserService,
                             SettingsService settingsService) {
        this.postRepository = postRepository;
        this.currentUserService = currentUserService;
        this.settingsService = settingsService;
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getAllStatistics() {
        if (!settingsService.getSettings().isStatisticsIsPublic() &&
                !(currentUserService.getCurrentUser().isPresent() &&
                        currentUserService.getCurrentUser().get().getIsModerator())) {
            throw new UnauthorizedException();
        }
        return calculateStats(postRepository.findAllActivePosts());
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getUserStatistics() {
        if (currentUserService.getCurrentUser().isEmpty()) {
            throw new UnauthorizedException();
        }
        UserEntity currentUser = currentUserService
                .getCurrentUser()
                .orElseThrow(UnauthorizedException::new);
        return calculateStats(postRepository.findActivePostsByUser(currentUser));
    }

    private StatisticsResponse calculateStats(List<PostEntity> posts) {
        int postsCount = posts.size();
        int likesCount = (int) posts
                .stream()
                .map(PostEntity::getPostVoteEntities)
                .flatMap(List::stream)
                .filter(v -> v.getValue() == 1)
                .count();
        int dislikesCount = (int) posts
                .stream()
                .map(PostEntity::getPostVoteEntities)
                .flatMap(List::stream)
                .filter(v -> v.getValue() == -1)
                .count();
        int viewsCount = posts
                .stream()
                .map(PostEntity::getViewCount)
                .reduce(Integer::sum)
                .orElse(0);
        long firstPostTimestamp = posts
                .stream()
                .map(e -> e.getTime()
                        .toEpochSecond(ZoneOffset.UTC))
                .reduce(Long::min)
                .orElse(0L);
        return new StatisticsResponse(postsCount, likesCount, dislikesCount, viewsCount, firstPostTimestamp);
    }
}
