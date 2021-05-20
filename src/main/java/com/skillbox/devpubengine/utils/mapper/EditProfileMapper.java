package com.skillbox.devpubengine.utils.mapper;

import com.skillbox.devpubengine.api.request.general.EditProfileRequest;
import com.skillbox.devpubengine.api.request.general.EditProfileWithPhotoRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface EditProfileMapper {

    @Mapping(source = "requestWithPhoto.name", target = "name")
    @Mapping(source = "requestWithPhoto.email", target = "email")
    @Mapping(source = "requestWithPhoto.password", target = "password")
    @Mapping(source = "requestWithPhoto.removePhoto", target = "removePhoto")
    @Mapping(source = "photoPath", target = "photo")
    EditProfileRequest simplify (EditProfileWithPhotoRequest requestWithPhoto, String photoPath);
}
