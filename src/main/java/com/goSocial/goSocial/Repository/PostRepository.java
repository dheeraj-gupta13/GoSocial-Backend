package com.goSocial.goSocial.Repository;

import com.goSocial.goSocial.Dto.PostResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deletePost(int postId) {
        String sql = "DELETE FROM posts WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    public boolean checkPostOwnership(int postId, int userId) {
        String sql = "SELECT COUNT(*) FROM posts WHERE post_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId, userId);
        return count != null && count > 0;
    }

    public List<PostResponse> getPostsByUser(int userId) {

        System.out.println("getPostsByUser PostRepository " + userId);
        String sql = """
            SELECT p.post_id, p.content, p.image_url, p.created_on,
                   u.user_id, u.username, pr.avatar_url
            FROM posts p
            JOIN users u ON p.user_id = u.user_id
            JOIN profile pr ON p.user_id = pr.user_id
            WHERE p.user_id = ?
            ORDER BY p.created_on DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PostResponse post = new PostResponse();
            post.setPostId(rs.getInt("post_id"));
            post.setContent(rs.getString("content"));
            post.setImageUrl(rs.getString("image_url"));
            post.setCreatedAt(rs.getTimestamp("created_on").toLocalDateTime());
            post.setUserId(rs.getInt("user_id"));
            post.setUsername(rs.getString("username"));
            post.setAvatarUrl(rs.getString("avatar_url"));
            return post;
        }, userId);
    }
}
