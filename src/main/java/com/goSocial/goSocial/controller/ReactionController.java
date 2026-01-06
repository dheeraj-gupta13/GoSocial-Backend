package com.goSocial.goSocial.controller;

import com.goSocial.goSocial.Dto.ApiResponse;
import com.goSocial.goSocial.service.ReactionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
class ReactedPost {
    private int post_id;
    private int reaction_type;
}

@RestController
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    // Add / update reaction
    @PostMapping("/reaction")
    public  ResponseEntity<ApiResponse<?>> addReaction(@RequestBody ReactedPost body) {
        reactionService.addReaction(body.getPost_id(), body.getReaction_type());
        return ResponseEntity.ok(new ApiResponse<>(true, "Reaction added successfully", body.getPost_id()));
    }

    // Remove reaction
    @DeleteMapping("/reaction")
    public ResponseEntity<ApiResponse<?>> removeReaction(@RequestParam int post_id) {
        System.out.println("Did we made it ?" + post_id);
        reactionService.removeReaction(post_id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reaction removed successfully", post_id));
    }

    // Get all reactions
    @GetMapping("/reaction")
    public ResponseEntity<ApiResponse<?>>getReactions(@RequestParam int post_id) {
        List<String> reactions = reactionService.getReactions(post_id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reactions fetched", reactions)
        );
    }

     // Get current user's reaction
    @GetMapping("/reactionType")
    public ResponseEntity<?> getReaction(@RequestParam int post_id) {
        Integer reaction = reactionService.getReactionType(post_id);
        return ResponseEntity.ok(Map.of("reaction_type", reaction));
    }
}
