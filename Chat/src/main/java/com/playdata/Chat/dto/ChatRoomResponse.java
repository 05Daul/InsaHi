package com.playdata.Chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomResponse {
    private String roomId;
    private String roomName;
    private LocalDateTime createdAt;
    private List<String> chatId;
    private int unreadCount;
    private String creatorName;
    private List<String> name;
    private String lastMessage;
}
