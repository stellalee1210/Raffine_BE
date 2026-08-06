package com.example.communityapplication.repository;

import com.example.communityapplication.dto.CommentResponseDto;
import com.example.communityapplication.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostId(Long postId);

    @Query("""
    SELECT new com.example.communityapplication.dto.CommentResponseDto(
        c.id,
        c.userId,
        u.nickname,
        c.createdAt,
        c.content
    )
    FROM Comments c
    LEFT OUTER JOIN Users u ON c.userId = u.id
    WHERE c.postId = :postId
    ORDER BY c.createdAt DESC
""")
    List<CommentResponseDto> findAllCommentList(@Param("postId") Long postId);
}
