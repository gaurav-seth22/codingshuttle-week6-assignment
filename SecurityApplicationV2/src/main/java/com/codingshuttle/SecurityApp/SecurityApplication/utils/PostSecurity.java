package com.codingshuttle.SecurityApp.SecurityApplication.utils;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.PostDTO;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;


    public boolean isOwnerOfPost(Long postId) {
        // Get the authentication object first
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Then get the principal and cast it to User
        User user = (User) authentication.getPrincipal();
        PostDTO post = postService.getPostById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }
}
