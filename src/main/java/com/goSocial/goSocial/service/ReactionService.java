package com.goSocial.goSocial.service;

import com.goSocial.goSocial.Exception.BadRequestException;
import com.goSocial.goSocial.Exception.InternalServerException;
import com.goSocial.goSocial.Exception.ResourceNotFoundException;
import com.goSocial.goSocial.Repository.ReactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionService {


    private final ReactionRepository repository;

    public ReactionService(ReactionRepository repository) {
        this.repository = repository;
    }

    // add a reaction
    public void addReaction(int post_id, int reaction_type) {

        if(reaction_type < 1 || reaction_type > 5){
            throw new BadRequestException("invalid reaction type");
        }

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication add comment " + authentication);

            String userIdStr = authentication.getName();
            int user_id = Integer.parseInt(userIdStr);


            System.out.println("add reaction service "  + user_id);

            repository.deleteReaction(post_id, user_id);
            repository.addReaction(post_id, user_id, reaction_type);
        }
        catch (Exception e){
            throw new InternalServerException("Failed to add reaction");
        }
    }

    // remove a reaction
    public void removeReaction(int post_id) {

        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication remove comment " + authentication);

            String userIdStr = authentication.getName();
            int user_id = Integer.parseInt(userIdStr);

            System.out.println("remove reaction service "  + user_id + ", " + post_id);

             repository.deleteReaction(post_id, user_id);
        }
        catch (Exception e){
            throw new InternalServerException("Failed to remove exception");
        }
    }

    // get a list of reactions of a particular post.
    public List<String> getReactions(int post_id) {

        List<String> reactions = repository.getReactions(post_id);
        if(reactions.isEmpty()){
            throw new ResourceNotFoundException("No reactions found for this post");
        }

        return reactions;
    }

    // get a reaction of a particular user for a particular post.
    public Integer getReactionType(int post_id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication add comment " + authentication);

        String userIdStr = authentication.getName();
        int user_id = Integer.parseInt(userIdStr);


        System.out.println("add reaction service "  + user_id);

        Integer reaction = repository.getReactionType(post_id, user_id);
        if(reaction == null){
            return -1;
        }

        return reaction;
    }

}
