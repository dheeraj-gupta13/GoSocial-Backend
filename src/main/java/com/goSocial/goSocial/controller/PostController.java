package com.goSocial.goSocial.controller;

import com.goSocial.goSocial.Dto.ApiResponse;
import com.goSocial.goSocial.Dto.PostResponseDto;
import com.goSocial.goSocial.model.Post;
import com.goSocial.goSocial.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/post/getAllPosts")
    public ResponseEntity<ApiResponse<?>> getAllPosts(){

        PostResponseDto postResponseDto = postService.getAllPosts();


        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Posts fetched successfully",
                        postResponseDto
                )
        );
    }

    @PostMapping("/post")
    public String addPost(@RequestParam("file") MultipartFile file){
         return postService.addPost(file);
    }

    @DeleteMapping("/post")
    public ResponseEntity<ApiResponse<?>> deletePost(@RequestParam int user_id){
        postService.deletePost(user_id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Post deleted successfully", null)
        );
    }

    @GetMapping("/post/getUserPosts")
    public ResponseEntity<ApiResponse<?>> getUsersPost(@RequestParam int user_id){

        System.out.println("hit hit hit " + user_id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Posts fetched successfully",
                        postService.getUserPosts(user_id)
                )
        );
    }
}
