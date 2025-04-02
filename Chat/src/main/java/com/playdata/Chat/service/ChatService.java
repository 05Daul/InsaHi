package com.playdata.Chat.service;

import com.playdata.Chat.entity.ChatMessage;
import com.playdata.Chat.repository.ChatMessageRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ChatService {

  private final Logger log = LoggerFactory.getLogger(ChatService.class);
  private final ChatMessageRepository chatMessageRepository;
  private final MongoTemplate mongoTemplate;

  public ChatMessage saveMessage(ChatMessage message) {
    // 메시지 보낸 사람 본인은 이미 읽었다고 처리
    if (!message.getReadBy().contains(message.getName())) {
      message.getReadBy().add(message.getName());
    }
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
      return chatMessageRepository.save(message);
    } else {
      throw new RuntimeException("메시지를 찾을 수 없습니다.");
    }
  }

  public void markMessageAsRead(String roomId, String name) {
    List<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId);
    for (ChatMessage message : messages) {
      if (!message.getReadBy().contains(name)) {
        message.getReadBy().add(name);
        chatMessageRepository.save(message);
      }
    }
  }
}

