package com.skillbox.devpubengine.service.general;

import com.skillbox.devpubengine.api.response.general.SettingsResponse;
import com.skillbox.devpubengine.model.GlobalSettingsEntity;
import com.skillbox.devpubengine.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final GlobalSettingsRepository settingsRepository;

    public SettingsService(GlobalSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsResponse getSettings() {
        SettingsResponse response = new SettingsResponse();
        for (GlobalSettingsEntity e : settingsRepository.findAll()) {
            boolean value = e.getValue().equalsIgnoreCase("yes");
            switch (e.getCode()) {
                case "MULTIUSER_MODE":
                    response.setMultiuserMode(value);
                    break;
                case "POST_PREMODERATION":
                    response.setPostPremoderation(value);
                    break;
                case "STATISTICS_IS_PUBLIC":
                    response.setStatisticsIsPublic(value);
                    break;
            }
        }
        return response;
    }
}
