package com.playdata.Chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Document(collection = "messages")  // ✅ MongoDB 컬렉션
public class ChatMessage {

    @Id
    private String chatId;  // ✅ MongoDB는 String 타입 ID를 주로 사용

    private String name;
    private String roomId;
    private String content;
    private String imageUrl; // 이미지 저장 URL 추가
    private LocalDateTime createdAt;
    private List<String> readBy = new ArrayList<>();
    private Boolean deleted; //false면 미삭제,true면 삭제

    }

