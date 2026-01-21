package com.goSocial.goSocial.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private int postId;
    private int userId;
    private String username;
    private String content;
    private String imageUrl;
    private String avatarUrl;
    private LocalDateTime createdAt;
}
