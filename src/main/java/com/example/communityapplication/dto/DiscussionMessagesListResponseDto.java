package com.example.communityapplication.dto;

import com.example.communityapplication.entity.DiscussionMessage;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.UsersRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DiscussionMessagesListResponseDto {
    private List<DiscussionMessageResponseDto> messageList;

    public DiscussionMessagesListResponseDto(List<DiscussionMessageResponseDto> messageList) {
        this.messageList = messageList;
    }

    public DiscussionMessagesListResponseDto(List<DiscussionMessage> recentMessageList, UsersRepository usersRepository) {
        this.messageList = recentMessageList.stream()
                .map(message -> {
                    Users user = usersRepository.findById(message.getUserId())
                            .orElseThrow(() -> new IllegalArgumentException("user not found"));

                    return new DiscussionMessageResponseDto(
                            "CHAT",
                            message.getUserId(),
                            user.getNickname(),
                            message.getContent(),
                            message.getCreatedAt()
                    );
                }).toList();
    }
}
