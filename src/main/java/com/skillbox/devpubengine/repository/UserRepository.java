package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    Optional<UserEntity> findByEmail(String email);
}
