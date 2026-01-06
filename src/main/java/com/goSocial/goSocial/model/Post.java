package com.goSocial.goSocial.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "user_id")
    private int userId;

    private String username;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // Stored as JSON in DB
//    @Convert(converter = ReactionListConverter.class)
    @ElementCollection
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Column(columnDefinition = "jsonb")
    private List<ReactionUtil> reactions;

    @Column(name = "do_i_follow")
    private int doIFollow;

    // Getters & Setters
}

