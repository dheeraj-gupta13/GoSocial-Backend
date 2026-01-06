package com.goSocial.goSocial.Repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReactionRepository {


    private final JdbcTemplate jdbcTemplate;

    public ReactionRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addReaction(int post_id, int user_id,  int reaction_type){
        String sql = """
            INSERT INTO postreactions (post_id, user_id, reaction_type, created_on)
            VALUES (?, ?, ?, NOW())
        """;
        jdbcTemplate.update(sql, post_id, user_id, reaction_type);
    }

    public void deleteReaction(int post_id, int user_id) {
        String sql = "DELETE FROM postreactions WHERE post_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, post_id, user_id);
    }

    public List<String> getReactions(int postId) {
        String sql = """
            SELECT u.username
            FROM postreactions pr
            JOIN users u ON pr.user_id = u.user_id
            WHERE pr.post_id = ?
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("username"),
                postId
        );
    }

    public Integer getReactionType(int postId, int userId) {
        String sql = "SELECT reaction_type FROM postreactions WHERE post_id=? AND user_id=?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, postId, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
