package com.goSocial.goSocial.service;

import com.cloudinary.Cloudinary;
import com.goSocial.goSocial.Dto.Profile;
import com.goSocial.goSocial.Dto.ProfileResponse;
import com.goSocial.goSocial.Dto.UpdateProfileResponse;
import com.goSocial.goSocial.Dto.UserMini;
import com.goSocial.goSocial.Exception.InternalServerException;
import com.goSocial.goSocial.Exception.ResourceNotFoundException;
import com.goSocial.goSocial.Repository.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    ProfileRepository profileRepository;
    private final Cloudinary cloudinary;

    public ProfileService(ProfileRepository profileRepository, Cloudinary cloudinary){
        this.profileRepository = profileRepository;
        this.cloudinary = cloudinary;
    }


    public void follow(int user_to_follow_id){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication add comment " + authentication);

            String userIdStr = authentication.getName();  // <-- THIS IS "6"
            int user_id = Integer.parseInt(userIdStr);
//            commentRepository.addComment(post_id,userId, comment);
            System.out.println("user_id " + user_id);
            System.out.println("user_to_follow_id " + user_to_follow_id);
            profileRepository.follow(user_id, user_to_follow_id);
        }
        catch (Exception e){
            throw new InternalServerException("Failed to follow.");
        }
    }


    public void unFollow(int user_to_unfollow_id){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication add comment " + authentication);

            String userIdStr = authentication.getName();  // <-- THIS IS "6"
            int user_id = Integer.parseInt(userIdStr);
//            commentRepository.addComment(post_id,userId, comment);
            profileRepository.unFollow(user_id, user_to_unfollow_id);
        }
        catch (Exception e){
            throw new InternalServerException("Failed to unfollow.");
        }
    }

    public ProfileResponse getProfile(String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication add comment " + authentication);

        String userIdStr = authentication.getName();  // <-- THIS IS "6"
        int current_user_id = Integer.parseInt(userIdStr);

        Integer userId = profileRepository.getUserIdByUsername(username);
        if (userId == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Profile profile = profileRepository.getProfileByUserId(userId);

        List<UserMini> followers = profileRepository.getFollowers(userId);
        List<UserMini> followings = profileRepository.getFollowings(userId);

        int doIFollow = profileRepository.checkIfFollowing(current_user_id, userId);

        ProfileResponse response = new ProfileResponse();
        response.setProfile(profile);
        response.setFollowers(followers);
        response.setFollowings(followings);
        response.setDoIFollow(doIFollow);

        return response;
    }

    public UpdateProfileResponse updateProfile(
            int userId,
            String username,
            String biodata,
            MultipartFile profilePic,
            MultipartFile backgroundPic
    ) {

        Profile existing = profileRepository.getProfileByUserId(userId);
        if (existing == null) {
            throw new ResourceNotFoundException("User profile not found");
        }

        String avatarUrl = existing.getAvatarUrl();
        String backgroundUrl = existing.getBackgroundUrl();

        try {
            if (profilePic != null && !profilePic.isEmpty()) {
                avatarUrl = uploadToCloudinary(profilePic, "profiles");
            }

            if (backgroundPic != null && !backgroundPic.isEmpty()) {
                backgroundUrl = uploadToCloudinary(backgroundPic, "backgrounds");
            }

            profileRepository.updateProfile(
                    userId,
                    username,
                    biodata,
                    avatarUrl,
                    backgroundUrl
            );

            return new UpdateProfileResponse(
                    userId,
                    username,
                    avatarUrl,
                    backgroundUrl,
                    biodata
            );

        } catch (Exception e) {
            throw new InternalServerException("Failed to update profile");
        }
    }

    private String uploadToCloudinary(MultipartFile file, String folder) throws Exception {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of("folder", folder)
        );
        return uploadResult.get("secure_url").toString();
    }

}
