package com.playdata.ElectronicApproval.exception;

/**
 * 존재하지 않는 결재 문서를 조회하거나 처리할 경우 발생하는 예외입니다.
 */
public class ApprovalNotFoundException extends RuntimeException {

  public ApprovalNotFoundException() {
    super("해당 결재 문서를 찾을 수 없습니다.");
  }

  public ApprovalNotFoundException(String message) {
    super(message);
  }

  public ApprovalNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApprovalNotFoundException(Throwable cause) {
    super(cause);
  }
}
