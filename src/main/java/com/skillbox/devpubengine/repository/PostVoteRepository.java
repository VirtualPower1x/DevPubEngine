package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.PostVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVoteEntity, Integer> {
}
