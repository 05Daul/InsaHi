package com.playdata.Chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageResponse {
    private String messageId;
    private String name;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
    private boolean deleted;
    private List<String> readBy;
}
