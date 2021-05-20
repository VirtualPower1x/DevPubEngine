package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.PostVoteEntity;
import com.skillbox.devpubengine.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVoteEntity, Integer> {

    Optional<PostVoteEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
