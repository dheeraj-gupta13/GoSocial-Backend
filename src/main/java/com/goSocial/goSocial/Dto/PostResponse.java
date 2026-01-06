package com.goSocial.goSocial.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private int postId;
    private int userId;
    private String username;
    private String content;
    private String imageUrl;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
