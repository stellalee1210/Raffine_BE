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
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    private Date createdAt;
    private String title;
    private String content;
    private String fileKey;

    private Date updatedAt;
    private Date deletedAt;

    public Posts(Long userId, Date createdAt, String title, String content, String fileKey) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.fileKey = fileKey;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public void updatePost(String newTitle, String newContent, String newFileKey){
        if(newTitle != null && !newTitle.equals(this.title)) this.changeTitle(newTitle);
        if(newContent != null && !newContent.equals(this.content)) this.changeContent(newContent);
        if(newFileKey != null && !newFileKey.equals(this.fileKey)) this.changeFile(newFileKey);
        this.updatedAt = new Date();
    }

    private void changeTitle(String title) {
        this.title = title;
    }
    private void changeContent(String content) {
        this.content = content;
    }
    private void changeFile(String fileKey){ this.fileKey = fileKey;}
}
