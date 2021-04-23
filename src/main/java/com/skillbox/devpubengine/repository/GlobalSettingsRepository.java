package com.skillbox.devpubengine.repository;

import com.skillbox.devpubengine.model.GlobalSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettingsEntity, Integer> {
}
