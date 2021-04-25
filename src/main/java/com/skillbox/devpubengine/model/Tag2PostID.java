package com.skillbox.devpubengine.model;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Tag2PostID implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    public Tag2PostID () {

    }

    public Tag2PostID(PostEntity post, TagEntity tag) {
        this.post = post;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public TagEntity getTag() {
        return tag;
    }

    public void setTag(TagEntity tag) {
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tag2PostID && ((Tag2PostID) obj).post.equals(this.post) &&
                ((Tag2PostID) obj).tag.equals(this.tag);
    }
}
