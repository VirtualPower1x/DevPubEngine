package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_DATE ")
    Page<PostEntity> findAllActivePosts(Pageable pageable);

    @Query(value = "SELECT p " +
            "FROM PostEntity p " +
            "LEFT JOIN p.postVoteEntities pv ON pv.value = 1 " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_DATE " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv) DESC ")
    Page<PostEntity> findAllActivePostsOrderByLikesCount(Pageable pageable);

    @Query(value = "SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_DATE " +
            "AND (p.title LIKE :query OR p.text LIKE :query)")
    Page<PostEntity> findActivePostsContainingStringIgnoreCase(Pageable pageable, @Param("query") String query);

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_DATE " +
            "AND p.time BETWEEN :timeAfter AND :timeBefore")
    Page<PostEntity> findActivePostsByTime(Pageable pageable,
                                           @Param("timeAfter") LocalDateTime timeAfter,
                                           @Param("timeBefore") LocalDateTime timeBefore);

    @Query(value = "SELECT p " +
            "FROM PostEntity p " +
            "LEFT JOIN p.tag2PostEntities ttp " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_DATE " +
            "AND ttp.tag2PostID.tag.name = :tagName")
    Page<PostEntity> findActivePostsByTag(Pageable pageable, @Param("tagName") String tag);
}
