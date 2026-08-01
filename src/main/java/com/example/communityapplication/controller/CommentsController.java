package com.example.communityapplication.controller;

import com.example.communityapplication.dto.CommentRequestDto;
import com.example.communityapplication.dto.CommentResponseDto;
import com.example.communityapplication.dto.CommentsListResponseDto;
import com.example.communityapplication.dto.EmptyResponseDto;
import com.example.communityapplication.response.ApiResponse;
import com.example.communityapplication.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
@CrossOrigin
public class CommentsController {
    private final CommentService commentService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@PathVariable Long postId, @PathVariable Long userId, @Valid @RequestBody CommentRequestDto request){
        CommentResponseDto commentResponse = commentService.createComment(postId, userId, request.getContent());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of("post_success", commentResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CommentsListResponseDto>> getComment(@PathVariable Long postId){
        CommentsListResponseDto commentResponse = commentService.getComment(postId);
        return ResponseEntity
                .ok(ApiResponse.of("get_success", commentResponse));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentsListResponseDto>> patchComment(@PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody CommentRequestDto request){
        CommentsListResponseDto commentResponse = commentService.patchComment(postId, commentId, request.getContent());
        return ResponseEntity
                .ok(ApiResponse.of("comment_patch_success", commentResponse));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<EmptyResponseDto>> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
