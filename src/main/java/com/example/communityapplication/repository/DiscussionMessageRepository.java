package com.example.communityapplication.repository;

import com.example.communityapplication.entity.DiscussionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionMessageRepository extends JpaRepository<DiscussionMessage, Long> {
    List<DiscussionMessage> findTop50ByOrderByCreatedAtDesc();
}
