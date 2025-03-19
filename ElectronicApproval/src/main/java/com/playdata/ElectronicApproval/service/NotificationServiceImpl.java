package com.playdata.ElectronicApproval.service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 결재 완료/반려 시 알림을 담당하는 서비스
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

/*
  private final JavaMailSender mailSender;

  // 이메일 알림 전송
  public void sendEmailNotification(String recipientEmail, String subject, String content) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(recipientEmail);
      helper.setSubject(subject);
      helper.setText(content, true); // HTML 형식 허용

      mailSender.send(message);
      log.info("[이메일 전송 성공] 받는 사람: {}, 제목: {}", recipientEmail, subject);
    } catch (Exception e) {
      log.error("[이메일 전송 실패] 받는 사람: {}, 에러: {}", recipientEmail, e.getMessage(), e);
    }
  }
*/




  /*
   * 결재 완료 알림
   */
  /*

  public void notifyApprovalCompleted(String recipientId, String approvalFileId) {
    sendApprovalNotification(recipientId, approvalFileId, "결재가 최종 승인되었습니다.");
  }
  */
  /*
   * 결재 반려 알림
   */
  /*
  public void notifyApprovalRejected(String recipientId, String approvalFileId, String reason) {
    sendApprovalNotification(recipientId, approvalFileId, "결재가 반려되었습니다. 사유: " + reason);
  }
  */
}

