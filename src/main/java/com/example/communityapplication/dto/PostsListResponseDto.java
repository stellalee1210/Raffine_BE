package com.example.communityapplication.dto;

import com.example.communityapplication.entity.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostsListResponseDto {
    private List<PostListDto> postList;
    public PostsListResponseDto(List<Posts> postList){
        this.postList=postList.stream()
                .map(post-> new PostListDto(
                        post.getId(),
                        post.getUserId(),
                        post.getAuthor(),
                        post.getFile(),
                        post.getTitle()
                )).toList();
    }
}
