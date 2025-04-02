package com.playdata.Chat.controller;

import com.playdata.Chat.dto.ChatRoomResponse;
import com.playdata.Chat.dto.RoomCreateRequest;
import com.playdata.Chat.entity.ChatRoom;
import com.playdata.Chat.repository.ChatRoomRepository;
import com.playdata.Chat.service.ChatRoomService;
import java.util.List;

import com.playdata.Chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

  private final ChatRoomService chatRoomService;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatService chatService;

  @PostMapping
  public ChatRoom createRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
    return chatRoomService.createRoom(roomCreateRequest.getRoomName(),
        roomCreateRequest.getMembers(), roomCreateRequest.getCreatorName());

  }

  @GetMapping("/member/{name}")
  public List<ChatRoomResponse> getRoomsForMember(@PathVariable("name") String name) {
    return chatRoomService.getRoomsForMember(name);
  }

  @PostMapping("/{roomId}/members")
  public ChatRoom addMemberToRoom(@PathVariable("roomId") String roomId, @RequestParam String Id) {
    return chatRoomService.addMember(roomId, Id);
  }

  @DeleteMapping("/{roomId}/members/{name}")
  public ChatRoom removeMemberFromRoom(@PathVariable("roomId") String roomId,
      @PathVariable("name") String name) {
    return chatRoomService.removeMember(roomId, name);
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<ChatRoom> getRoomById(@PathVariable("roomId") String roomId) {
    return chatRoomRepository.findById(roomId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
  @PostMapping("/{roomId}/read")
  public ResponseEntity<?> markAsRead(@PathVariable("roomId") String roomId, @RequestParam String name) {
    chatService.markMessageAsRead(roomId, name);
    return ResponseEntity.ok().build();
  }


}
