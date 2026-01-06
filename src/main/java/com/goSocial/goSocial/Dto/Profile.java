package com.goSocial.goSocial.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Profile {
    private int profileId;
    private int userId;
    private String username;
    private String avatarUrl;
    private String backgroundUrl;
    private String biodata;
    private LocalDateTime createdOn;
}
