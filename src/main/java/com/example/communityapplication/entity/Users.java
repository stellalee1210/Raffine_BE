package com.example.communityapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@RequiredArgsConstructor
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String profilePicture;
    private Date deletedAt;

    public Users(String email, String password, String nickname, String profilePicture){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profilePicture = profilePicture == null? "default_picture.jpg" : profilePicture;
        this.deletedAt = null;
    }

    public void changePassword(String password) {
        this.password = password;
    }
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changeProfilePicture(String profile_picture) {
        this.profilePicture = profile_picture;
    }

}
