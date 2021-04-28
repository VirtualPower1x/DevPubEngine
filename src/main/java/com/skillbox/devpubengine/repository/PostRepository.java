package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.PostEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP ")
    Collection<PostEntity> findAllActivePosts();

    @Query("SELECT p " +
            "FROM PostEntity p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= CURRENT_TIMESTAMP ")
    Collection<PostEntity> findAllActivePosts(Sort sort);

    @Query(value = "SELECT * " +
            "FROM posts " +
            "WHERE is_active = 1 " +
            "AND moderation_status = 'ACCEPTED' " +
            "AND time <= CURRENT_TIMESTAMP " +
            "ORDER BY " +
                "(SELECT q.c " +
                "FROM " +
                    "(SELECT posts.id, count(value) AS c " +
                    "FROM posts " +
                    "RIGHT JOIN post_votes ON posts.id = post_votes.post_id " +
                    "WHERE value = 1 " +
                    "GROUP BY posts.id) AS q " +
                "WHERE posts.id = q.id) " +
            "DESC", nativeQuery = true)
    Collection<PostEntity> findAllSortedByLikesCount();
}
