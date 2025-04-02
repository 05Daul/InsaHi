package com.playdata.Chat.repository;

import com.playdata.Chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

  List<ChatRoom> findByNameContaining(String name);
}