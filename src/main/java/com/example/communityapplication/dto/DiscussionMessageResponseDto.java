package com.example.communityapplication.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionMessageResponseDto {
    private String type;
    private Long userId;
    private String nickname;
    private String content;
    private Date createdAt;
}
