package com.example.communityapplication.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String nickname;
    private String password;
    private String profilePicture;
}
