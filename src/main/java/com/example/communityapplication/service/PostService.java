package com.example.communityapplication.service;

import com.example.communityapplication.dto.PostResponseDto;
import com.example.communityapplication.dto.PostUpdateResponseDto;
import com.example.communityapplication.dto.PostsListResponseDto;
import com.example.communityapplication.entity.Posts;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.PostsRepository;
import com.example.communityapplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    private final CommentService commentService;

    @PreAuthorize("@userAuthChecker.isOwner(#userId, authentication.name)")
    public PostResponseDto createPost(Long userId, String title, String content, String file) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        String author = user.getNickname();
        Posts post = new Posts(
                userId,
                author,
                new Date(),
                title,
                content,
                file
        );
        postsRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostsListResponseDto getPostList() {
        List<Posts> postList = postsRepository.findAll();
        return new PostsListResponseDto(postList);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        return new PostResponseDto(post);
    }

    @PreAuthorize("@postAuthChecker.isOwner(#postId, authentication.name)")
    public PostUpdateResponseDto updatePost(Long postId, String newTitle, String newContent, String newFile) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found - post update unavailable"));
        post.updatePost(newTitle, newContent,  newFile);
        return new PostUpdateResponseDto(post);
    }

    @PreAuthorize("@postAuthChecker.isOwner(#postId, authentication.name)")
    public void deletePost(Long postId) {
        //댓글 먼저 전부 삭제 후 -> 게시글 삭제
        commentService.deleteAllCommentFromPost(postId);

        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        postsRepository.delete(post);
    }

    public void deleteAllPostFromUser(Long userId){
        List<Posts> postList = postsRepository.findByUserId(userId);
        for (Posts post : postList) {
            postsRepository.delete(post);
        }
    }
}
