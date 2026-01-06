package com.goSocial.goSocial.Dto;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private int comment_id;
    private String comment;
    private String username;
    private String created_at;
    private String avatar_url;
}
