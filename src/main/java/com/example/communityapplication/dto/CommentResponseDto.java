package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String author;
    private String content;
    private Date createdAt;

    public CommentResponseDto(Long id,
                              Long userId,
                              String author,
                              Date createdAt,
                              String content){
        this.id = id;
        this.userId = userId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }
}
