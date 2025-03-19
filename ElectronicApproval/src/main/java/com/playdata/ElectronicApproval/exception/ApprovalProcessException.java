package com.playdata.ElectronicApproval.exception;

/**
 * 결재 처리 중 발생하는 일반적인 비즈니스 예외입니다. 예: 결재 요청 실패, 승인/반려 처리 실패 등
 */
public class ApprovalProcessException extends RuntimeException {

  // 기본 메시지 생성자
  public ApprovalProcessException() {
    super("결재 처리 과정에서 오류가 발생했습니다.");
  }

  // 사용자 정의 메시지 생성자
  public ApprovalProcessException(String message) {
    super(message);
  }

  // 원인 포함 생성자
  public ApprovalProcessException(String message, Throwable cause) {
    super(message, cause);
  }

  // 원인만 포함하는 생성자
  public ApprovalProcessException(Throwable cause) {
    super(cause);
  }
}
