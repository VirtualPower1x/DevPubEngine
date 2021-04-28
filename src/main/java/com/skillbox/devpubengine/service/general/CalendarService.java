package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.response.general.CalendarResponse;
import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final PostRepository postRepository;

    public CalendarService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CalendarResponse getCalendar (Integer year) {
        if (year == null) {
            year = LocalDateTime.now().getYear();
        }
        final int yearValue = year;
        List<PostEntity> posts = postRepository.findAll()
                .stream()
                .filter(e -> e.getIsActive() == 1 &&
                        e.getModerationStatus() == ModerationStatus.ACCEPTED &&
                        !e.getTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        HashSet<Integer> years = new HashSet<>();
        for (PostEntity post : posts) {
            years.add(post.getTime().getYear());
        }
        HashMap<String, Integer> postsByDate = posts.stream()
                .filter(e -> e.getTime().getYear() == yearValue)
                .map(e -> new SimpleEntry<>(convertLocalDateTimeToString(e.getTime()), 1))
                .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue, Integer::sum, HashMap::new));
        return new CalendarResponse(years, postsByDate);
    }

    private String convertLocalDateTimeToString(LocalDateTime ldt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        return (sdf.format(Date.from(instant)));
    }
}
