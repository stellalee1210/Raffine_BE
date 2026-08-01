package com.example.communityapplication.dto;


import com.example.communityapplication.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profilePicture;

    public UserResponseDto(Users user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profilePicture = user.getProfilePicture();
    }
}