package com.goSocial.goSocial.Repository;

import com.goSocial.goSocial.Dto.CommentDto;
import com.goSocial.goSocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository{

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUserName(String username){
        String sql = """
                SELECT * FROM users WHERE username = ?;
                """;
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {
                    User currentUser = new User();

                    currentUser.setUser_id(rs.getInt("user_id"));
                    currentUser.setUsername(rs.getString("username"));
                    currentUser.setPassword(rs.getString("password"));
                    currentUser.setEmail(rs.getString("email"));
                    currentUser.setCreated_on(rs.getString("created_on"));
                    currentUser.setIs_email_verified(rs.getString("is_email_verified"));
                    return currentUser;
                }, username);

    }


}
