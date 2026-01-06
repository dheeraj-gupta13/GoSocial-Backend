package com.goSocial.goSocial.service;

import com.goSocial.goSocial.Dto.CommentDto;
import com.goSocial.goSocial.Exception.InternalServerException;
import com.goSocial.goSocial.Exception.ResourceNotFoundException;
import com.goSocial.goSocial.Repository.CommentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public void addComment(int post_id, String comment){

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication add comment " + authentication);

            String userIdStr = authentication.getName();  // <-- THIS IS "6"
            int userId = Integer.parseInt(userIdStr);
            commentRepository.addComment(post_id,userId, comment);
        }
        catch (Exception e){
            throw new InternalServerException("Failed to add comment.");
        }

    }

    public List<CommentDto> getCommentsForPost(int post_id){
        List<CommentDto> comments = commentRepository.getCommentsForPost(post_id);

        System.out.println("cs " + post_id);
        System.out.println("cs2 " + comments.size());

        return comments;
    }
}
