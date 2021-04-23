package com.skillbox.devpubengine.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_moderator", nullable = false)
    private byte isModerator;

    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;

    private String photo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostEntity> postEntities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostCommentEntity> postCommentEntities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostVoteEntity> postVoteEntities = new ArrayList<>();

    public UserEntity () {

    }

    public UserEntity(byte isModerator, LocalDateTime regTime, String name, String email, String password) {
        this.isModerator = isModerator;
        this.regTime = regTime;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(byte isModerator) {
        this.isModerator = isModerator;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<PostEntity> getPostEntities() {
        return postEntities;
    }

    public void setPostEntities(List<PostEntity> postEntities) {
        this.postEntities = postEntities;
    }

    public List<PostCommentEntity> getPostCommentEntities() {
        return postCommentEntities;
    }

    public void setPostCommentEntities(List<PostCommentEntity> postCommentEntities) {
        this.postCommentEntities = postCommentEntities;
    }

    public List<PostVoteEntity> getPostVoteEntities() {
        return postVoteEntities;
    }

    public void setPostVoteEntities(List<PostVoteEntity> postVoteEntities) {
        this.postVoteEntities = postVoteEntities;
    }
}
