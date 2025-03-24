package com.playdata.ElectronicApproval.controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/sse")
public class SseController {

  //  private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  // 클라이언트가 연결을 요청하면 Emitter를 반환
  @GetMapping(value = "/subscribe/{employeeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@PathVariable String employeeId) {
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

    emitters.computeIfAbsent(employeeId, id -> new CopyOnWriteArrayList<>()).add(emitter);

    emitter.onCompletion(() -> removeEmitter(employeeId, emitter));
    emitter.onTimeout(() -> removeEmitter(employeeId, emitter));

    System.out.println("Employee " + employeeId + " subscribed!");

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

  public void notifyUser(String employeeId, String message) {
    List<SseEmitter> userEmitters = emitters.get(employeeId);

    if (userEmitters != null) {
      userEmitters.forEach(emitter -> {
        try {
          emitter.send(SseEmitter.event().name("approval-update").data(message));
        } catch (IOException e) {
          removeEmitter(employeeId, emitter);
        }
      });
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
