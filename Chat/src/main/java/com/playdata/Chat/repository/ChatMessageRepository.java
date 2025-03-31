package com.playdata.Chat.repository;

import com.playdata.Chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByRoomId(String roomId);  // 특정 채팅방의 메시지 가져오기

    int findByRoomIdAndReadByNotContaining(String roomId, String name);
}
