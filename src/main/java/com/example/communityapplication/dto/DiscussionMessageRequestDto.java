package com.example.communityapplication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DiscussionMessageRequestDto {
    @NotBlank
    private String type;
    @NotBlank
    private String content;
}
