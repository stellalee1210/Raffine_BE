package com.example.communityapplication.dto;

import com.example.communityapplication.entity.Comments;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor

public class CommentsListResponseDto {
    private List<CommentResponseDto> commentsList;

    public CommentsListResponseDto(List<Comments> commentsList){
        this.commentsList=commentsList.stream()
                .map(comment -> new CommentResponseDto(
                        comment
                )).toList();
    }
}
