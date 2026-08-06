package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private Long userId;
    private String author;
    private Date createdAt;
    private String title;
    private String content;

    public PostResponseDto(
            Long id,
            Long userId,
            String author,
            Date createdAt,
            String title,
            String content
            ){
        this.id = id;
        this.userId= userId;
        this.author = author;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
    }
}
