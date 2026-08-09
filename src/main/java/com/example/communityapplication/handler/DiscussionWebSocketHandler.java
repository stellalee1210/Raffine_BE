package com.example.communityapplication.handler;

import com.example.communityapplication.dto.DiscussionMessageRequestDto;
import com.example.communityapplication.dto.DiscussionMessageResponseDto;
import com.example.communityapplication.entity.DiscussionMessage;
import com.example.communityapplication.entity.Users;
import com.example.communityapplication.repository.DiscussionMessageRepository;
import com.example.communityapplication.repository.UsersRepository;
import com.example.communityapplication.service.DiscussionSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DiscussionWebSocketHandler extends TextWebSocketHandler {

    private final DiscussionSessionManager discussionSessionManager;
    private final ObjectMapper objectMapper;
    private final UsersRepository usersRepository;
    private final DiscussionMessageRepository discussionMessageRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        discussionSessionManager.addSession(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        DiscussionMessageRequestDto requestDto =
                objectMapper.readValue(message.getPayload(), DiscussionMessageRequestDto.class);

        Long userId = (Long) session.getAttributes().get("userId");
        String type = requestDto.getType();
        String content = requestDto.getContent();
        Date createdAt = new Date();

        if(content == null || content.isBlank()){
            throw new IllegalArgumentException("session - content emtpy");
        }

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("session - user not found"));

        DiscussionMessage discussionMessage = new DiscussionMessage(type, userId, requestDto.getContent(), createdAt);
        discussionMessageRepository.save(discussionMessage);

        DiscussionMessageResponseDto responseDto = new DiscussionMessageResponseDto(
                requestDto.getType(),
                userId,
                user.getNickname(),
                requestDto.getContent(),
                createdAt
        );

        String responseMessage = objectMapper.writeValueAsString(responseDto);

        discussionSessionManager.broadcast(responseMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        discussionSessionManager.removeSession(session);
    }
}