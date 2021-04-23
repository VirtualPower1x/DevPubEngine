package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.PostCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostCommentEntity, Integer> {
}
