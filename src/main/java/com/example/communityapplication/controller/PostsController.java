package com.example.communityapplication.controller;

import com.example.communityapplication.dto.*;
import com.example.communityapplication.response.ApiResponse;
import com.example.communityapplication.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@CrossOrigin
public class PostsController {
    private final PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@PathVariable Long userId, @Valid @RequestBody PostRequestDto request) {
        PostResponseDto postResponse = postService.createPost(userId, request.getTitle(),request.getContent(),request.getFile());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of("post_success", postResponse));
    }

    //전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PostsListResponseDto>> getPostList() {
        PostsListResponseDto postLististResponse = postService.getPostList();
        return ResponseEntity
                .ok(ApiResponse.of("get_success", postLististResponse));
    }

    //상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(@PathVariable Long postId) {
        PostResponseDto postResponse = postService.getPost(postId);
        return ResponseEntity
                .ok(ApiResponse.of("get_success", postResponse));
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostUpdateResponseDto>> updatePost(@PathVariable Long postId, @Valid  @RequestBody PostUpdateRequestDto request) {
        PostUpdateResponseDto postResponse = postService.updatePost(postId, request.getTitle(),request.getContent(),request.getFile());
        return ResponseEntity
                .ok(ApiResponse.of("post_patch_success", postResponse));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<EmptyResponseDto>> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity
                .noContent().build();
    }
}
