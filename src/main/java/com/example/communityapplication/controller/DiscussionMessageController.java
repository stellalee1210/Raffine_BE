package com.example.communityapplication.controller;

import com.example.communityapplication.dto.DiscussionMessagesListResponseDto;
import com.example.communityapplication.response.ApiResponse;
import com.example.communityapplication.service.DiscussionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discussion/messages")
@RequiredArgsConstructor
@CrossOrigin
public class DiscussionMessageController {
    private final DiscussionMessageService discussionMessageService;

    @GetMapping
    public ResponseEntity<ApiResponse<DiscussionMessagesListResponseDto>> getDiscussionMessageList() {
        DiscussionMessagesListResponseDto discussionMessageList =
                discussionMessageService.getDiscussionMessageList();
        return ResponseEntity
                .ok(ApiResponse.of("get_success", discussionMessageList));
    }
}
