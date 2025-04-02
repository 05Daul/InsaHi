package com.playdata.Chat.controller;

import com.playdata.Chat.entity.ChatMessage;
import com.playdata.Chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/chat-room")
    public String serveChatTestHtml(){
        return "chat-room";
    }

    @MessageMapping("/chat/{roomId}")
    public void processMessage(ChatMessage message) {
        // 메시지에 누락된 데이터 채우기(서버에선 해당 사항을 못채움)
        message.setCreatedAt(LocalDateTime.now());        // 현재 시간 추가
        message.setDeleted(false);

        logger.info("WebSocket으로 받은 메시지: {}", message);
        simpMessagingTemplate.convertAndSend("/topic/messages", message); // 메시지 전송만 수행
    }
}
