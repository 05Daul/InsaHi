package com.playdata.Chat.controller;

import com.playdata.Chat.dto.ChatMessageResponse;
import com.playdata.Chat.entity.ChatMessage;
import com.playdata.Chat.service.ChatService;
import com.playdata.Chat.service.FileStorageService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ChatRestController {

  private final ChatService chatService;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final FileStorageService fileStorageService;

  // DB에서 roomName 컬럼만 distinct 추출
  @GetMapping("/rooms")
  public List<String> getChatRooms() {
    return chatService.findDistinctRoomNames();
  }

  //  특정 채팅방의 메시지 조회 (REST API)
  @GetMapping("/messages/{roomId}")
  public List<ChatMessage> getMessages(@PathVariable("roomId") String roomId) {
    return chatService.getMessagesByRoom(roomId);
  }

  @DeleteMapping("/messages/{chatId}")
  public ResponseEntity<?> deleteMessage(@PathVariable("chatId") String chatId) {
    try {
      ChatMessage updatedMessage = chatService.deleteMessage(chatId);

      // ✅ 삭제된 메시지를 WebSocket을 통해 전송하여 UI를 즉시 업데이트
      simpMessagingTemplate.convertAndSend("/topic/messages", updatedMessage);

      return ResponseEntity.ok(updatedMessage);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메시지를 찾을 수 없습니다.");
    }
  }

  @PostMapping("/send")
  public ResponseEntity<?> sendMessage(
      @RequestParam("name") String name,
      @RequestParam("roomId") String roomId,
      @RequestParam(value = "content", required = false) String content,
      @RequestParam(value = "image", required = false) MultipartFile imageFile) {

//        // 1) 이미지 저장
    String imageUrl = null;
    if (imageFile != null && !imageFile.isEmpty()) {
      imageUrl = fileStorageService.saveFile(imageFile);
      if (imageUrl == null) {
        // 바디가 null이면 프론트에서 .then(res => res.json())이 터짐 -> 최소한 JSON 객체라도 내리기
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", "이미지 저장 실패");
        return ResponseEntity.badRequest().body(errorBody);
      }
    }
//        // 2) DB 저장 (chatId는 MongoDB가 자동생성)
    ChatMessage chatMessage = chatService.saveMessage(
        new ChatMessage(null, name, roomId, content, imageUrl, LocalDateTime.now(), new ArrayList<>(), false));

    // 3) DB에 저장된 진짜 chatId를 가진 메시지를 WebSocket으로 broadcast
    simpMessagingTemplate.convertAndSend("/topic/messages/" + roomId, chatMessage);

    // 4) 클라이언트에 응답
    return ResponseEntity.ok(new ChatMessageResponse(
        chatMessage.getChatId(),
        chatMessage.getName(),
        chatMessage.getContent(),
        chatMessage.getImageUrl(),
        chatMessage.getCreatedAt(),
        chatMessage.getDeleted(),
            chatMessage.getReadBy()
    ));
  }
//    @GetMapping("/files/{storedFileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String storedFileName) {
//        try {
//            System.out.println("downloadFile => " + storedFileName);
//            // ✅ 실제 저장된 파일 찾기
//            File file = fileStorageService.getFile(storedFileName);
//            System.out.println("downloadFile => " + storedFileName);
//            System.out.println("FILE PATH => " + file.getAbsolutePath());
//            System.out.println("EXISTS? => " + file.exists());
//
//            if (!file.exists()) {
//                return ResponseEntity.notFound().build();
//            }
//
//            String encodedFileName = URLEncoder.encode(storedFileName, StandardCharsets.UTF_8).replace("+","%20");
//            Resource resource = new FileSystemResource(file);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
//                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                    .body(resource);
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();        }
//    }


}