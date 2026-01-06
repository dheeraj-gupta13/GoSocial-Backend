package com.goSocial.goSocial.Dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileResponse {
    private Profile profile;
    private List<UserMini> followers;
    private List<UserMini> followings;
    private int doIFollow;
}
