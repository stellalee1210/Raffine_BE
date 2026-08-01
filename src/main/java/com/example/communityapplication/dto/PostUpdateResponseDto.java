package com.example.communityapplication.dto;

import com.example.communityapplication.entity.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class PostUpdateResponseDto {
    private String title;
    private String content;
    private String file;
    private Date updatedAt;
    public PostUpdateResponseDto(Posts posts){
        this.updatedAt = posts.getUpdatedAt();
        this.title = posts.getTitle();
        this.content = posts.getContent();
        this.file = posts.getFile();
    }
}
