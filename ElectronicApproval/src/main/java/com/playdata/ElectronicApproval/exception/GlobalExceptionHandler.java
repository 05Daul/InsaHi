package com.playdata.ElectronicApproval.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApprovalLineNotFoundException.class)
  public ResponseEntity<String> handleLineNotFound(ApprovalLineNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(ApprovalNotFoundException.class)
  public ResponseEntity<String> handleApprovalNotFound(ApprovalNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(ApprovalPermissionDeniedException.class)
  public ResponseEntity<String> handlePermissionDenied(ApprovalPermissionDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
  }

  @ExceptionHandler(ApprovalAlreadyProcessedException.class)
  public ResponseEntity<String> handleAlreadyProcessed(ApprovalAlreadyProcessedException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
  }

  @ExceptionHandler(ApprovalProcessException.class)
  public ResponseEntity<String> handleProcessError(ApprovalProcessException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  // 기타 알 수 없는 예외 처리
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<String> handleGeneralException(Exception ex) {
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
//  }
}
