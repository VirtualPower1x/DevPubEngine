package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.Tag2PostEntity;
import com.skillbox.devpubengine.model.Tag2PostID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2PostEntity, Tag2PostID> {
}
