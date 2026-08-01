package com.example.communityapplication.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    @Size(min=1)
    @Size(max=26)
    private String title;

    private String content;
    private String file;
}
