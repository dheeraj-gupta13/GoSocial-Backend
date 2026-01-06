package com.goSocial.goSocial.controller;

import com.goSocial.goSocial.Dto.ApiResponse;
import com.goSocial.goSocial.Dto.CommentDto;
import com.goSocial.goSocial.service.CommentService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Setter
@Getter
class CommentRequest{
    private int post_id;
    private String comment;
}

@RestController
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService =  commentService;
    }

    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<?>>  addComment(@RequestBody CommentRequest request){
        System.out.println("Post ID: " + request.getPost_id());
        System.out.println("Comment: " + request.getComment());

        int post_id = request.getPost_id();
        String comment = request.getComment();
        commentService.addComment(post_id, comment);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comment added successfully", null)
        );
    }

    @GetMapping("/comment")
    public  ResponseEntity<ApiResponse<?>> getCommentsForPost(@RequestParam int post_id){
        List<CommentDto> comments =  commentService.getCommentsForPost(post_id);
        System.out.println("cc1 " + comments.size());
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Comments fetched successfully", comments)
        );
    }

}
