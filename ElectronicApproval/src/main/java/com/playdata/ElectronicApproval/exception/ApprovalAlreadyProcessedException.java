package com.playdata.ElectronicApproval.exception;

/**
 * 이미 처리된 결재에 대해 추가적인 승인/반려 요청이 들어올 경우 발생하는 예외입니다.
 */
public class ApprovalAlreadyProcessedException extends RuntimeException {

  public ApprovalAlreadyProcessedException() {
    super("이미 처리된 결재입니다.");
  }

  public ApprovalAlreadyProcessedException(String message) {
    super(message);
  }

  public ApprovalAlreadyProcessedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApprovalAlreadyProcessedException(Throwable cause) {
    super(cause);
  }
}
