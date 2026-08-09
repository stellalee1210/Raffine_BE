package com.example.communityapplication.config;

import com.example.communityapplication.handler.DiscussionWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class DiscussionWebSocketConfig implements WebSocketConfigurer {
    private final DiscussionWebSocketHandler discussionWebSocketHandler;
    private final DiscussionHandshakeInterceptor discussionHandshakeInterceptor;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler( discussionWebSocketHandler, "/ws/discussion")
                .addInterceptors(discussionHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:5173");
    }
}