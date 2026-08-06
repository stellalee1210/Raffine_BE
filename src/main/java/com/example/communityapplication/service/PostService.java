package com.example.communityapplication.service;

import com.example.communityapplication.dto.PostResponseDto;
import com.example.communityapplication.dto.PostUpdateResponseDto;
import com.example.communityapplication.dto.PostsListResponseDto;
import com.example.communityapplication.entity.Posts;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.PostsRepository;
import com.example.communityapplication.repository.UsersRepository;
import com.example.communityapplication.storage.LocalImageStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final LocalImageStorage localImageStorage;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    public PostResponseDto createPost(String email, String title, String content, MultipartFile inputFile) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));


        boolean uploaded = inputFile != null && !inputFile.isEmpty();

        String fileKey = uploaded ?
                localImageStorage.upload(inputFile, "post")
                : "default/default-picture.png";

        try{
            Posts post = postsRepository.save(
                    new Posts(
                            user.getId(),
                            new Date(),
                            title,
                            content,
                            fileKey
                    )
            );
            return new PostResponseDto(
                    post.getId(),
                    user.getId(),
                    user.getNickname(),
                    post.getCreatedAt(),
                    post.getTitle(),
                    post.getContent());

        } catch (RuntimeException e) {
            if(uploaded){
                localImageStorage.delete(fileKey);
            }
            throw e;
        }

    }

    @Transactional(readOnly = true)
    public PostsListResponseDto getPostList() {
        List<PostResponseDto> postList = postsRepository.findAllPostList();
        return new PostsListResponseDto(postList);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId) {
        PostResponseDto postResponseDto = postsRepository.findPost(postId);
        if(postResponseDto == null){
            throw new  IllegalArgumentException("post not found");
        }
        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public Resource getPostImageResource(Long postId){
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        return localImageStorage.get((post.getFileKey()));
    }

    @PreAuthorize("@postAuthChecker.isOwner(#postId, authentication.name)")
    public PostUpdateResponseDto updatePost(Long postId, String titleInput, String contentInput, MultipartFile fileInput) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found - post update unavailable"));
        boolean newImageUploaded =
                fileInput != null && !fileInput.isEmpty();
        String originalFileKey = post.getFileKey();
        String newFileKey = originalFileKey;
        if(newImageUploaded){
            newFileKey = localImageStorage.upload(fileInput, "post");
        }

        try{
            post.updatePost(titleInput, contentInput, newFileKey);
            postsRepository.flush();
        }catch (RuntimeException e){
            if(newImageUploaded){
                localImageStorage.delete((newFileKey));
            }
            throw e;
        }

        if (newImageUploaded
                && !"default/default-picture.png".equals(originalFileKey)) {
            localImageStorage.delete(originalFileKey);
        }

        return new PostUpdateResponseDto(post);
    }

    @PreAuthorize("@postAuthChecker.isOwner(#postId, authentication.name)")
    public void deletePost(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("post not found"));
        String fileKey = post.getFileKey();
        boolean uploadedImage = !"default/default-picture.png".equals(fileKey);


        //댓글 먼저 전부 삭제 후 -> 게시글 삭제
        commentService.deleteAllCommentFromPost(postId);
        postsRepository.delete(post);
        postsRepository.flush();

        if(uploadedImage){
            localImageStorage.delete(fileKey);
        }
    }

    public void deleteAllPostFromUser(Long userId){
        List<Posts> postList = postsRepository.findPostByUserId(userId);
        for (Posts post : postList) {
            postsRepository.delete(post);
        }
    }
}
