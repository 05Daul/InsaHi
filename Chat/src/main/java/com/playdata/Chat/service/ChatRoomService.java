package com.playdata.Chat.service;

import com.playdata.Chat.entity.ChatRoom;
import com.playdata.Chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

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

    public List<ChatRoom> getRoomsForMember(String employeeId) {
        return chatRoomRepository.findByMemberContaining(employeeId);
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
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));

    List<String> memberList = room.getName();
    if (memberList.contains(name)) {
    memberList.remove(name);
    room.setName(memberList);
    room = chatRoomRepository.save(room);
    }
    return room; //업데이트된 최종 방 반환
    }
}
