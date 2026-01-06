package com.goSocial.goSocial.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int user_id;
    private String username;
    private String password;
    private String email;
    private String created_on;
    private String is_email_verified;
}
