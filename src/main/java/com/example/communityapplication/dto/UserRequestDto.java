package com.example.communityapplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8)
    @Size(max = 16)
    @NotBlank
    private String password;

    @Size(min = 1)
    @Size(max = 10)
    @NotBlank
    private String nickname;

    @NotBlank
    private String profilePicture;
}