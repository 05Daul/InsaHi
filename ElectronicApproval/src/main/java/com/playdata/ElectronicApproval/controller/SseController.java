package com.playdata.ElectronicApproval.controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/approval/sse")
@Slf4j
public class SseController {

  //  private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  // 클라이언트가 연결을 요청하면 Emitter를 반환
  @GetMapping(value = "/subscribe/{employeeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@PathVariable("employeeId") String employeeId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    emitters.computeIfAbsent(employeeId, id -> new CopyOnWriteArrayList<>()).add(emitter);

    emitter.onCompletion(() -> {
      log.info("SSE 연결 완료됨(정상 종료): {}", employeeId);
      removeEmitter(employeeId, emitter);
    });

    emitter.onTimeout(() -> {
      log.warn("SSE 연결 타임아웃 발생: {}", employeeId);
      removeEmitter(employeeId, emitter);
    });
    System.out.println("Employee " + employeeId + " subscribed!");
    log.info("Employee {} subscribed!", employeeId);
    return emitter;
  }

  private void removeEmitter(String employeeId, SseEmitter emitter) {
    List<SseEmitter> userEmitters = emitters.get(employeeId);
    if (userEmitters != null) {
      userEmitters.remove(emitter);
      if (userEmitters.isEmpty()) {
        emitters.remove(employeeId);
      }
    }
  }

  @GetMapping("/test")
  public void testNotify() {
    notifyUser("2025ec06", "테스트 알림입니다!");
  }

  public void notifyUser(String employeeId, String message) {
    log.info("✅ 알림 전송 시도 - 대상: {}, 메시지: {}", employeeId, message);

    List<SseEmitter> userEmitters = emitters.get(employeeId);

    if (userEmitters != null) {
      userEmitters.forEach(emitter -> {
        try {
          log.info("SSE 전송 중... to approval-update::message{}", message);
          emitter.send(SseEmitter.event().name("approval-update").data(message));
        } catch (IOException e) {
          log.error("SSE 전송 중 IOException 발생 - 연결 종료로 추정, employeeId={}, message={}, error={}",
              employeeId, message, e.getMessage());
          removeEmitter(employeeId, emitter);
        }
      });
    } else {
      log.warn("⚠️ 해당 employeeId에 대한 emitter가 없습니다: {}", employeeId);
    }
  }

  public void broadcastToAll(String message) {
    emitters.forEach((employeeId, userEmitters) -> {
      userEmitters.forEach(emitter -> {
        try {
          emitter.send(SseEmitter.event().name("broadcast").data(message));
        } catch (IOException e) {
          removeEmitter(employeeId, emitter);
        }
      });
    });
  }

}
