package com.example.communityapplication.controller;

import com.example.communityapplication.dto.*;
import com.example.communityapplication.response.ApiResponse;
import com.example.communityapplication.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@CrossOrigin
public class PostsController {
    private final PostService postService;

    @PostMapping(
            value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            Authentication authentication,
            @Valid @RequestPart("request") PostRequestDto request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        PostResponseDto postResponse = postService.createPost(authentication.getName(), request.getTitle(),request.getContent(), file);
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

    @GetMapping("/{postId}/image")
    public ResponseEntity<Resource> getPostImage(@PathVariable Long postId) throws IOException {
        Resource resource = postService.getPostImageResource(postId);

        Path imagePath = resource.getFile().toPath();
        String contentType = Files.probeContentType(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    //게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostUpdateResponseDto>> updatePost(@PathVariable Long postId, @Valid  @RequestBody PostUpdateRequestDto request) {
        PostUpdateResponseDto postResponse = postService.updatePost(postId, request.getTitle(),request.getContent(),request.getFileKey());
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
