package com.goSocial.goSocial.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {


    private final JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    public void register(String username, String password){

        System.out.println("username : " + username);
        System.out.println("password : " + password);
        String sql = """
                 INSERT INTO users (email, username, password, created_on)
                 VALUES (?, ?, ?, NOW())
                """;

        jdbcTemplate.update(sql, "", username, password);


        int user_id = getUserId(username);

        String query = """
                INSERT INTO profile (user_id, avatar_url, background_url, biodata, created_on) 
                VALUES (?, ? , ? , ?, NOW());
                """;

        jdbcTemplate.update(
                query,
                user_id,
                "https://res.cloudinary.com/dol59d0b3/image/upload/v1755956191/profiles/blob.png",
                "https://res.cloudinary.com/dol59d0b3/image/upload/v1755957313/backgrounds/blob.jpg",
                "Hey!"
        );

    }

    public int getUserId(String username) {

        String sql = """
                SELECT user_id FROM users WHERE username = ?
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class, username);
    }

    public boolean existsByUsername(String username){
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }
}
