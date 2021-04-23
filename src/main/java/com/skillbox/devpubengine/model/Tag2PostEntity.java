package com.skillbox.devpubengine.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tag2post")
public class Tag2PostEntity {
    @EmbeddedId
    private Tag2PostID tag2PostID;

    public Tag2PostEntity () {

    }

    public Tag2PostID getTag2PostID() {
        return tag2PostID;
    }

    public void setTag2PostID(Tag2PostID tag2PostID) {
        this.tag2PostID = tag2PostID;
    }
}
