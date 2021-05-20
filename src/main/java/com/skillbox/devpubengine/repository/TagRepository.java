package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.TagEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    @Override
    @Query("SELECT t FROM TagEntity t")
    List<TagEntity> findAll(Sort sort);

    Optional<TagEntity> findByName(String name);
}
