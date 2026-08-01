package com.example.communityapplication.security;

import com.example.communityapplication.entity.Posts;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.PostsRepository;
import com.example.communityapplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("postAuthChecker")
@RequiredArgsConstructor
public class PostAuthChecker {

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;

    public boolean isOwner(Long postId, String name) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));

        Users user = usersRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        return post.getUserId().equals(user.getId());
    }
}