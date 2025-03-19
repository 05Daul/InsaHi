package com.playdata.ElectronicApproval.exception;

/**
 * 결재 라인을 찾을 수 없을 때 발생하는 예외입니다.
 */
public class ApprovalLineNotFoundException extends RuntimeException {

  public ApprovalLineNotFoundException() {
    super("결재 라인을 찾을 수 없습니다.");
  }

  public ApprovalLineNotFoundException(String message) {
    super(message);
  }

  public ApprovalLineNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApprovalLineNotFoundException(Throwable cause) {
    super(cause);
  }
}
