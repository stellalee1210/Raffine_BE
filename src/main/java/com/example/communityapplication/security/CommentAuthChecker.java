package com.example.communityapplication.security;

import com.example.communityapplication.entity.Comments;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.CommentsRepository;
import com.example.communityapplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("commentAuthChecker")
@RequiredArgsConstructor
public class CommentAuthChecker {

    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;

    public boolean isOwner(Long commentId, String email) {
        Comments comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("comment not found"));

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        return comment.getUserId().equals(user.getId());
    }
}