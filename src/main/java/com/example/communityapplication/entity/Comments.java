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
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private Long userId;
    private String author;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public Comments(Long postId,Long userId, String author, String content, Date createdAt){
        this.postId = postId;
        this.userId = userId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public void update (String content){
        this.changeContent(content);
        this.updateDate();
    }
    private void changeContent(String content){
        this.content = content;
    }
    private void updateDate() {this.updatedAt = new Date();}
}
