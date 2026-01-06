package com.goSocial.goSocial.service;

import com.cloudinary.utils.ObjectUtils;
import com.goSocial.goSocial.Dto.PostResponse;
import com.goSocial.goSocial.Dto.PostResponseDto;
import com.goSocial.goSocial.Exception.ResourceNotFoundException;
import com.goSocial.goSocial.Repository.PostRepository;
import com.goSocial.goSocial.model.Post;
import com.goSocial.goSocial.model.Reaction;
import com.goSocial.goSocial.model.ReactionUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;



@Service
public class PostService {

    private final JdbcTemplate jdbcTemplate;
    private PostRepository postRepository;
    private final Cloudinary cloudinary;


    public PostService(JdbcTemplate jdbcTemplate, Cloudinary cloudinary, PostRepository postRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.cloudinary = cloudinary;
        this.postRepository = postRepository;
    }


    public String addPost(MultipartFile file){
        System.out.println(cloudinary);

        try{
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            return imageUrl;
        }
        catch (Exception e){
            throw new RuntimeException("Image upload failed", e);
        }

    }

    public void deletePost(int post_id){
        int user_id = -1;
        boolean exists = postRepository.checkPostOwnership(post_id, user_id);
        if (!exists) {
            throw new ResourceNotFoundException("Post not found or unauthorized");
        }

        postRepository.deletePost(post_id);
    }

    public List<PostResponse> getUserPosts(int user_id) {

        List<PostResponse> posts = postRepository.getPostsByUser(user_id);

        if (posts.isEmpty()) {
            throw new ResourceNotFoundException("No posts found for this user");
        }

        return posts;
    }

    public PostResponseDto getAllPosts(){

        int currentUserId = getCurrentUserId(); // mocked
        String currentUsername = getCurrentUsername();

        String query = """
            SELECT p.post_id, p.content, p.image_url, p.created_on,
                   p.user_id, u.username, profile.avatar_url
            FROM posts p
            JOIN users u ON p.user_id = u.user_id
            JOIN profile ON p.user_id = profile.user_id
        """;

//        PostResponseDto postResponseDto = new PostResponseDto();

        List<Post> posts = jdbcTemplate.query(query, (rs, rowNum) -> {
            Post post = new Post();

            post.setPostId(rs.getInt("post_id"));
            post.setContent(rs.getString("content"));
            post.setImageUrl(rs.getString("image_url"));
            post.setCreatedAt(formatTime(rs.getTimestamp("created_on")));
            post.setUserId(rs.getInt("user_id"));
            post.setUsername(rs.getString("username"));
            post.setAvatarUrl(rs.getString("avatar_url"));

            post.setReactions(fetchReactions(post.getPostId()));
            post.setDoIFollow(checkFollow(currentUserId, post.getUserId()));

            return post;
        });

        return new PostResponseDto(currentUserId, currentUsername, posts);
    }

    private List<ReactionUtil> fetchReactions(int postId) {
        String query = """
            SELECT u.username, p.reaction_type
            FROM postreactions p
            JOIN users u ON p.user_id = u.user_id
            WHERE p.post_id = ?
        """;

        return jdbcTemplate.query(query, (rs, rowNum) ->
                new ReactionUtil(rs.getString("username"), rs.getInt("reaction_type")), postId);
    }

    private int checkFollow(int currentUserId, int postUserId) {
        String sql = "SELECT id FROM followers WHERE following_user_id=? AND followed_user_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, currentUserId, postUserId);
        } catch (Exception e) {
            System.out.println("----- " + currentUserId + " --- " + postUserId);
            return currentUserId == postUserId ? -1 : 0;
        }
    }

    private String formatTime(Timestamp createdAt) {
        long diff = System.currentTimeMillis() - createdAt.getTime();
        long minutes = diff / (1000 * 60);

        if (minutes < 1) return "now";
        if (minutes < 60) return minutes + "m";
        if (minutes < 1440) return (minutes / 60) + "h";
        if (minutes < 2880) return "yesterday";
        if (minutes < 43200) return (minutes / 1440) + "d";
        return (minutes / 525600) + "y";
    }

    private int getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication add comment " + authentication);

        String userIdStr = authentication.getName();  // <-- THIS IS "6"
        int userId = Integer.parseInt(userIdStr);
        return userId;
    }

    private String getCurrentUsername() {
        return "dheeraj"; // Replace with auth context
    }
}
