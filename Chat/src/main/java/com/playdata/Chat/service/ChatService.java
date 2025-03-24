package com.playdata.Chat.service;

import com.playdata.Chat.entity.ChatMessage;
import com.playdata.Chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

    public ChatMessage saveMessage(ChatMessage message) {
            return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesByRoom(String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }
    public List<String> findDistinctRoomNames() {
        return mongoTemplate.query(ChatMessage.class)
                .distinct("roomName")
                .as(String.class)
                .all();
    }
    public ChatMessage deleteMessage(String chatId) {
        Optional<ChatMessage> existingMessage = chatMessageRepository.findById(chatId);
        if (existingMessage.isPresent()) {
            ChatMessage message = existingMessage.get();
            message.setDeleted(true);
            message.setContent("삭제된 메시지입니다.");
            return chatMessageRepository.save(message);
        } else {
            throw new RuntimeException("메시지를 찾을 수 없습니다.");
        }
    }

}
