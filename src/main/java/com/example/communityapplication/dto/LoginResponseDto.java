package com.example.communityapplication.dto;

import com.example.communityapplication.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private Long id;

    public LoginResponseDto(Users user){
        this.id = user.getId();
    }
}
