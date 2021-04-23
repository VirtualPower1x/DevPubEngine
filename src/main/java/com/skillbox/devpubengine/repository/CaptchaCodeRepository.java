package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.CaptchaCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaCodeRepository extends JpaRepository<CaptchaCodeEntity, Integer> {
}