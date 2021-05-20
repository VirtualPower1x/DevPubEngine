package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.PostEntity;
import com.skillbox.devpubengine.model.Tag2PostEntity;
import com.skillbox.devpubengine.model.Tag2PostID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2PostEntity, Tag2PostID> {

    @Query("SELECT t " +
            "FROM Tag2PostEntity t " +
            "WHERE t.tag2PostID.post = :post")
    List<Tag2PostEntity> findAllByPost(@Param("post") PostEntity post);
}
