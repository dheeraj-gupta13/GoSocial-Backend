package com.goSocial.goSocial.controller;

import com.goSocial.goSocial.Dto.ApiResponse;
import com.goSocial.goSocial.Dto.ProfileResponse;
import com.goSocial.goSocial.Dto.UpdateProfileResponse;
import com.goSocial.goSocial.service.ProfileService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
class FollowRequest {
    int user_id;
}

@RestController
public class ProfileController {

    ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PostMapping("/follow")
    public ResponseEntity<ApiResponse<?>> follow(@RequestBody FollowRequest request){
        System.out.println("entry " + request.getUser_id());
        profileService.follow(request.getUser_id());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Followed successfully", null)
        );
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<ApiResponse<?>> unFollow(@RequestBody FollowRequest request){
        System.out.println("entry " + request.getUser_id());
        profileService.unFollow(request.getUser_id());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "unFollowed successfully", null)
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getProfile(@RequestParam String username) {
        ProfileResponse profile = profileService.getProfile(username);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Profile fetched successfully", profile)
        );
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<ApiResponse<?>> updateProfile(
            @RequestParam("username") String new_username,
            @RequestParam("biodata") String biodata,
            @RequestParam(value = "profile_pic", required = false) MultipartFile profile_pic,
            @RequestParam(value = "background_pic", required = false) MultipartFile background_pic,

            @RequestParam(value = "removeProfilePic", defaultValue = "false") boolean removeProfilePic,
            @RequestParam(value = "removeBackgroundPic", defaultValue = "false") boolean removeBackgroundPic,

            Authentication authentication
    ) {

        String userIdStr = authentication.getName();
        int user_id = Integer.parseInt(userIdStr);

        UpdateProfileResponse response =
                profileService.updateProfile(user_id, new_username, biodata, profile_pic, background_pic, removeProfilePic, removeBackgroundPic);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Profile updated successfully", response)
        );
    }
}
