package com.skillbox.devpubengine.service.post;

import com.skillbox.devpubengine.api.request.post.GetPostsByDateRequest;
import com.skillbox.devpubengine.api.response.post.PostData;
import com.skillbox.devpubengine.api.response.post.PostsResponse;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.repository.PostRepository;
import com.skillbox.devpubengine.utils.mapper.PostMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPostsByDateService{

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public GetPostsByDateService(PostRepository postRepository, PostMapper postMapper) {

        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Transactional(readOnly = true)
    public PostsResponse getPostsByDate (GetPostsByDateRequest request) {
        if (request.getDate().equals("")) {
            return new PostsResponse();
        }
        LocalDateTime dateValue = parseLocalDateTimeFromString(request.getDate());
        int pageCount = request.getOffset() / request.getLimit();
        Pageable pageable = PageRequest.of(pageCount, request.getLimit(), Sort.by("time").descending());
        Page<PostEntity> page = postRepository.findActivePostsByTime(pageable, dateValue, dateValue.plusDays(1));
        List<PostData> posts = page
                .stream()
                .map(postMapper::postEntityToPostData)
                .collect(Collectors.toList());
        return new PostsResponse((int) page.getTotalElements(), posts);
    }

    private LocalDateTime parseLocalDateTimeFromString (String source) {
        DateTimeFormatterFactory factory = new DateTimeFormatterFactory();
        factory.setPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = factory.createDateTimeFormatter();
        return LocalDateTime.of(LocalDate.parse(source, formatter), LocalTime.MIN)
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
