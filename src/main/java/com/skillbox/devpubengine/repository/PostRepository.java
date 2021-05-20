package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.ModerationStatus;
import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP ")
    List<PostEntity> findAllActivePosts();

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP ")
    Page<PostEntity> findAllActivePosts(Pageable pageable);

    @Query(value = "SELECT p " +
            "FROM PostEntity p " +
            "LEFT JOIN p.postVoteEntities pv ON pv.value = 1 " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv) DESC ")
    Page<PostEntity> findAllActivePostsOrderByLikesCount(Pageable pageable);

    @Query(value = "SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP " +
            "AND (p.title LIKE :query OR p.text LIKE :query)")
    Page<PostEntity> findActivePostsContainingStringIgnoreCase(Pageable pageable, @Param("query") String query);

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP " +
            "AND p.time BETWEEN :timeAfter AND :timeBefore")
    Page<PostEntity> findActivePostsByTime(Pageable pageable,
                                           @Param("timeAfter") LocalDateTime timeAfter,
                                           @Param("timeBefore") LocalDateTime timeBefore);

    @Query(value = "SELECT p " +
            "FROM PostEntity p " +
            "LEFT JOIN p.tag2PostEntities ttp " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP " +
            "AND ttp.tag2PostID.tag.name = :tagName")
    Page<PostEntity> findActivePostsByTag(Pageable pageable, @Param("tagName") String tag);

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = true " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP " +
            "AND p.user = :user")
    List<PostEntity> findActivePostsByUser(@Param("user") UserEntity user);

    Page<PostEntity> findAllByUserAndIsActive(Pageable pageable, UserEntity user, boolean isActive);

    Page<PostEntity> findAllByUserAndIsActiveAndModerationStatus(Pageable pageable, UserEntity user, boolean isActive,
                                                                 ModerationStatus status);

    Page<PostEntity> findAllByIsActiveIsTrueAndModerationStatus(Pageable pageable, ModerationStatus status);

    Page<PostEntity> findAllByIsActiveIsTrueAndModerationStatusAndModeratorId(Pageable pageable,
                                                                              ModerationStatus status,
                                                                              int moderatorId);

    int countAllByModerationStatus(ModerationStatus status);
}
