package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostListDto {
    private Long id;
    private Long userId;
    private String author;
    private String file;
    private String title;

    public PostListDto(Long id, Long userId, String author, String file, String title) {
        this.id =id;
        this.userId= userId;
        this.author = author;
        this.title = title;
        this.file = file;
    }
}
