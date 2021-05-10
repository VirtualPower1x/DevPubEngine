package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.CaptchaCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCodeEntity, Integer> {

    @Query("SELECT c " +
            "FROM CaptchaCodeEntity c " +
            "WHERE c.time < :timeValue")
    List<CaptchaCodeEntity> getAllByTimeBefore (@Param("timeValue") LocalDateTime timeValue);


    Optional<CaptchaCodeEntity> findBySecretCode(String secretCode);
}
