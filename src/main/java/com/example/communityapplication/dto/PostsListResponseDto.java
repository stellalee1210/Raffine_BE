package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsListResponseDto {
    private List<PostListDto> postList;
    public PostsListResponseDto(List<PostResponseDto> postList){
        this.postList=postList.stream()
                .map(postResponseDto-> new PostListDto(
                        postResponseDto.getId(),
                        postResponseDto.getUserId(),
                        postResponseDto.getAuthor(),
                        postResponseDto.getTitle()
                )).toList();
    }
}
