package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfilePictureResponseDto {
    private String profilePicture;
    public ProfilePictureResponseDto(String profilePicture){
        this.profilePicture = profilePicture;
    }
}
