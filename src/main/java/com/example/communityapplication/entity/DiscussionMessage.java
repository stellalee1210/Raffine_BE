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
public class DiscussionMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private Long userId;
    private String content;
    private Date createdAt;

    public DiscussionMessage(String type, Long userId, String content, Date createdAt){
        this.type = type;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
