package com.goSocial.goSocial.Repository;

import com.goSocial.goSocial.Dto.Profile;
import com.goSocial.goSocial.Dto.UserMini;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileRepository {


    private final JdbcTemplate jdbcTemplate;

    public ProfileRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void getProfile(){

    }

    public void follow(int user_id, int user_to_follow_id){
        String sql = """
                INSERT INTO followers (following_user_id, followed_user_id, created_at) 
                VALUES (?,?, NOW()) 
               """;

        jdbcTemplate.update(sql, user_id, user_to_follow_id);
    }

    public void unFollow(int user_id, int user_to_unfollow_id){

        String sql = """
                DELETE FROM followers WHERE following_user_id = ? AND followed_user_id = ?
               """;

        jdbcTemplate.update(sql, user_id, user_to_unfollow_id);
    }

    public void updateProfile(){

    }

    public Integer getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Profile getProfileByUserId(int userId) {
        String sql = """
            SELECT p.profile_id, p.user_id, p.avatar_url, p.background_url,
                   p.biodata, p.created_on, u.username
            FROM profile p
            JOIN users u ON p.user_id = u.user_id
            WHERE u.user_id = ?
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Profile p = new Profile();
            p.setProfileId(rs.getInt("profile_id"));
            p.setUserId(rs.getInt("user_id"));
            p.setAvatarUrl(rs.getString("avatar_url"));
            p.setBackgroundUrl(rs.getString("background_url"));
            p.setBiodata(rs.getString("biodata"));
            p.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
            p.setUsername(rs.getString("username"));
            return p;
        }, userId);
    }

    public List<UserMini> getFollowers(int userId) {
        String sql = """
            SELECT u.user_id, u.username
            FROM followers f
            JOIN users u ON f.following_user_id = u.user_id
            WHERE f.followed_user_id = ?
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserMini u = new UserMini();
            u.setUserId(rs.getInt("user_id"));
            u.setUsername(rs.getString("username"));
            return u;
        }, userId);
    }

    public List<UserMini> getFollowings(int userId) {
        String sql = """
            SELECT u.user_id, u.username
            FROM followers f
            JOIN users u ON f.followed_user_id = u.user_id
            WHERE f.following_user_id = ?
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserMini u = new UserMini();
            u.setUserId(rs.getInt("user_id"));
            u.setUsername(rs.getString("username"));
            return u;
        }, userId);
    }

    public int checkIfFollowing(int currentUserId, int targetUserId) {

        if (currentUserId == targetUserId) {
            return -1;
        }

        String sql = "SELECT 1 FROM followers WHERE following_user_id=? AND followed_user_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, currentUserId, targetUserId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public Profile getProfileByUserId2(int userId) {
        String sql = """
            SELECT p.user_id, p.avatar_url, p.background_url, p.biodata
            FROM profile p
            WHERE p.user_id = ?
        """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Profile p = new Profile();
            p.setUserId(rs.getInt("user_id"));
            p.setAvatarUrl(rs.getString("avatar_url"));
            p.setBackgroundUrl(rs.getString("background_url"));
            p.setBiodata(rs.getString("biodata"));
            return p;
        }, userId);
    }

    public void updateProfile(int userId, String username, String bio,
                              String avatarUrl, String backgroundUrl) {

        String sql = """
            UPDATE profile
            SET biodata = ?, avatar_url = ?, background_url = ?
            WHERE user_id = ?
        """;

        jdbcTemplate.update(sql, bio, avatarUrl, backgroundUrl, userId);
    }

}
