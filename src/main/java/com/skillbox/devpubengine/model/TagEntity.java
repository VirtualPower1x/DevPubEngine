package com.skillbox.devpubengine.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag2PostID.tag", cascade = CascadeType.ALL)
    private List<Tag2PostEntity> tag2PostEntities = new ArrayList<>();

    public TagEntity() {

    }

    public TagEntity(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag2PostEntity> getTag2PostEntities() {
        return tag2PostEntities;
    }

    public void setTag2PostEntities(List<Tag2PostEntity> tag2PostEntities) {
        this.tag2PostEntities = tag2PostEntities;
    }
}
