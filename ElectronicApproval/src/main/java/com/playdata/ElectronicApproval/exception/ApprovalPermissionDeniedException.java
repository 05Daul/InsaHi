package com.playdata.ElectronicApproval.exception;

/**
 * 결재 권한이 없는 사용자가 승인 또는 반려를 시도할 경우 발생하는 예외입니다.
 */
public class ApprovalPermissionDeniedException extends RuntimeException {

  public ApprovalPermissionDeniedException() {
    super("결재 권한이 없습니다.");
  }

  public ApprovalPermissionDeniedException(String message) {
    super(message);
  }

  public ApprovalPermissionDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApprovalPermissionDeniedException(Throwable cause) {
    super(cause);
  }
}
