package com.skillbox.devpubengine.api.request.general;

import org.springframework.web.multipart.MultipartFile;

public class EditProfileWithPhotoRequest {

    private String name;

    private String email;

    private String password = null;

    private Integer removePhoto = null;

    private MultipartFile photo = null;

    public EditProfileWithPhotoRequest() {

    }

    public EditProfileWithPhotoRequest(String name, String email, String password, Integer removePhoto, MultipartFile photo) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.removePhoto = removePhoto;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRemovePhoto() {
        return removePhoto;
    }

    public void setRemovePhoto(Integer removePhoto) {
        this.removePhoto = removePhoto;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
