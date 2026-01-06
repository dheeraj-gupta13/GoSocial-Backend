package com.goSocial.goSocial.Repository;

import com.goSocial.goSocial.Dto.CommentDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addComment(int post_id, int user_id, String comment){

        String sql = """
                 INSERT INTO comments (post_id, user_id, comment, created_at)
                 VALUES (?, ?, ?, NOW())
                """;

        jdbcTemplate.update(sql, post_id, user_id, comment);
    }

    public List<CommentDto> getCommentsForPost(int post_id){

        String sql = """
                    SELECT c.comment_id, u.username, c.comment, c.created_at, p.avatar_url
                    	FROM comments c
                    	JOIN users u ON c.user_id = u.user_id
                    	JOIN profile p ON p.user_id = u.user_id
                    	WHERE post_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    CommentDto comment = new CommentDto();

                    comment.setComment_id(rs.getInt("comment_id"));
                    comment.setComment(rs.getString("comment"));
                    comment.setUsername(rs.getString("username"));
                    comment.setCreated_at(rs.getString("created_at"));
                    comment.setAvatar_url(rs.getString("avatar_url"));
                    return comment;
        }, post_id);
    }
}
