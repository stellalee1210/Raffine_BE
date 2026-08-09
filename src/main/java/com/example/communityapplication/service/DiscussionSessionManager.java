package com.example.communityapplication.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DiscussionSessionManager {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void broadcast(String message) throws IOException {
        // 열린 세션들에게 TextMessage 전송
        for(WebSocketSession session : sessions){
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}