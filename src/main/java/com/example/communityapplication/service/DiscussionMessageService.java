package com.example.communityapplication.service;

import com.example.communityapplication.dto.DiscussionMessagesListResponseDto;
import com.example.communityapplication.entity.DiscussionMessage;
import com.example.communityapplication.repository.DiscussionMessageRepository;
import com.example.communityapplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
@Transactional
public class DiscussionMessageService {
    private final DiscussionMessageRepository discussionMessageRepository;
    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    public DiscussionMessagesListResponseDto getDiscussionMessageList() {
        List<DiscussionMessage> recentMessageList =
                discussionMessageRepository.findTop50ByOrderByCreatedAtDesc();

        Collections.reverse(recentMessageList);

        return new DiscussionMessagesListResponseDto(recentMessageList, usersRepository);
    }
}
