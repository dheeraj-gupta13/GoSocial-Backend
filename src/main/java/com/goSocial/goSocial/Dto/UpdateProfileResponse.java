package com.goSocial.goSocial.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateProfileResponse {
    private int userId;
    private String username;
    private String avatarUrl;
    private String backgroundUrl;
    private String biodata;
}
