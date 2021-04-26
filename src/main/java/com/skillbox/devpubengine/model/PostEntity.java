package com.skillbox.devpubengine.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('NEW', 'ACCEPTED', 'DECLINED')", name = "moderation_status", nullable = false)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany(mappedBy = "tag2PostID.post", cascade = CascadeType.ALL)
    private List<Tag2PostEntity> tag2PostEntities = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostCommentEntity> postCommentEntities = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostVoteEntity> postVoteEntities = new ArrayList<>();

    public PostEntity() {

    }

    public PostEntity(byte isActive, ModerationStatus moderationStatus, UserEntity user, LocalDateTime time,
                      String title, String text, int viewCount) {
        this.isActive = isActive;
        this.moderationStatus = moderationStatus;
        this.user = user;
        this.time = time;
        this.title = title;
        this.text = text;
        this.viewCount = viewCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getIsActive() {
        return isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public void setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
    }

    public Integer getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(Integer moderatorId) {
        this.moderatorId = moderatorId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public List<Tag2PostEntity> getTag2PostEntities() {
        return tag2PostEntities;
    }

    public void setTag2PostEntities(List<Tag2PostEntity> tag2PostEntities) {
        this.tag2PostEntities = tag2PostEntities;
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
