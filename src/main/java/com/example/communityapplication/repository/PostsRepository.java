package com.example.communityapplication.repository;

import com.example.communityapplication.dto.PostResponseDto;
import com.example.communityapplication.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findPostByUserId(Long userId);

    @Query("""
        SELECT new com.example.communityapplication.dto.PostResponseDto(
            p.id,
            p.userId,
            u.nickname,
            p.createdAt,
            p.title,
            p.content
        )
        FROM Posts p
        LEFT OUTER JOIN Users u ON p.userId = u.id
        ORDER BY p.createdAt DESC
    """)
    List<PostResponseDto> findAllPostList();

    @Query("""
        SELECT new com.example.communityapplication.dto.PostResponseDto(
            p.id,
            p.userId,
            u.nickname,
            p.createdAt,
            p.title,
            p.content
        )
        FROM Posts p
        LEFT OUTER JOIN Users u ON p.userId = u.id
        WHERE p.id = :postId
    """)
    PostResponseDto findPost(@Param("postId") Long postId);
}
