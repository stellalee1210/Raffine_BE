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
    private String author;
    private Date createdAt;

    private String title;
    private String content;
    private String fileKey;
    private Date updatedAt;
    private Date deletedAt;

    public Posts(Long userId, String author, Date createdAt, String title, String content, String fileKey) {
        this.userId = userId;
        this.author = author;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.fileKey = fileKey;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public void updatePost(String newTitle, String newContent, String newFile){
        if(newTitle != null && !newTitle.equals(this.title)) this.changeTitle(newTitle);
        if(newContent != null && !newContent.equals(this.content)) this.changeContent(newContent);
        if(newFile != null && !newFile.equals(this.fileKey)) this.changeFile(newFile);
        this.updatedAt = new Date();
    }

    private void changeTitle(String title) {
        this.title = title;
    }
    private void changeContent(String content) {
        this.content = content;
    }
    private void changeFile(String file){ this.fileKey = file;}
}
