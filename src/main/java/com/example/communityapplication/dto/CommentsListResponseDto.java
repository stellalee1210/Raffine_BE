package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor

public class CommentsListResponseDto {
    private List<CommentResponseDto> commentsList;

    public CommentsListResponseDto(List<CommentResponseDto> commentsList){
        this.commentsList=commentsList.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getUserId(),
                        comment.getAuthor(),
                        comment.getCreatedAt(),
                        comment.getContent()
                )).toList();
    }
}
