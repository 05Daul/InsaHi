package com.playdata.Chat.service;

import com.playdata.Chat.dto.ChatRoomResponse;
import com.playdata.Chat.entity.ChatMessage;
import com.playdata.Chat.entity.ChatRoom;
import com.playdata.Chat.repository.ChatMessageRepository;
import com.playdata.Chat.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;

  //채팅방 생성 ( 원본 내용은 보존 )
  public ChatRoom createRoom(String roomName, List<String> name, String creatorName) {
    ChatRoom chatRoom = new ChatRoom();
    chatRoom.setRoomName(roomName);
    chatRoom.setName(name);
    chatRoom.setCreatorName(creatorName);
    chatRoom.setCreatedAt(LocalDateTime.now());
    ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
    return savedRoom;
  }

  public List<ChatRoomResponse> getRoomsForMember(String name) {
    List<ChatRoom> chatrooms = chatRoomRepository.findByNameContaining(name);
    List<ChatRoomResponse> responses = new ArrayList<>();

    for (ChatRoom room : chatrooms) {
      List<ChatMessage> messages = chatMessageRepository.findByRoomId(room.getRoomId());

      Integer count = chatMessageRepository.countByRoomIdAndReadByNotContaining(room.getRoomId(), name);
      int unreadCount = count != null ? count : 0;

      // 마지막 메시지 내용
      String lastMessageContent = "";
      ChatMessage lastMessage = chatMessageRepository.findTopByRoomIdOrderByCreatedAtDesc(room.getRoomId());
      if (lastMessage != null) {
        lastMessageContent = lastMessage.getContent();
      }

      ChatRoomResponse response = new ChatRoomResponse(
              room.getRoomId(),
              room.getRoomName(),
              room.getCreatedAt(),
              messages.stream().map(ChatMessage::getRoomId).collect(Collectors.toList()),
              unreadCount,
              room.getCreatorName(),
              room.getName(),
              lastMessageContent
      );

      responses.add(response);
    }
    return responses;
  }

  public ChatRoom addMember(String roomId, String newMemberName) {
    Optional<ChatRoom> optionalRoom = chatRoomRepository.findById(roomId);

    if (optionalRoom.isPresent()) {
      ChatRoom room = optionalRoom.get();
      List<String> memberList = room.getName();

      if (!memberList.contains(newMemberName)) {
        memberList.add(newMemberName);
        room.setName(memberList);
        return chatRoomRepository.save(room);
      }
      return room;
    } else {
      throw new RuntimeException("Room not found with id: " + roomId);
    }
  }

  public ChatRoom removeMember(String roomId, String name) {
    ChatRoom room = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

    List<String> memberList = room.getName();
    if (memberList.contains(name)) {
      memberList.remove(name);
      room.setName(memberList);
      room = chatRoomRepository.save(room);
    }
    return room; //업데이트된 최종 방 반환
  }
}