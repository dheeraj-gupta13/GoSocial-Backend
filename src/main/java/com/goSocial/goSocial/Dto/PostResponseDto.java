package com.goSocial.goSocial.Dto;

import com.goSocial.goSocial.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class PostResponseDto {
    int user_id;
    String username;
    List<Post> posts;
}
