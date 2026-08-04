package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostListDto {
    private Long id;
    private Long userId;
    private String author;
    private String fileKey;
    private String title;

    public PostListDto(Long id, Long userId, String author, String fileKey, String title) {
        this.id =id;
        this.userId= userId;
        this.author = author;
        this.title = title;
        this.fileKey = fileKey;
    }
}
