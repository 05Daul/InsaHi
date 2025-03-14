package com.playdata.HumanResourceManagement.department.deventAPI.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * RedisListener는 Redis Pub/Sub을 통해 발행된 메시지를 구독하고 처리하는 역할을 합니다.
 */
@Component
public class RedisListener implements MessageListener {

    /**
     * Redis에서 메시지를 수신하면 자동으로 실행되는 메서드
     *
     * @param message 수신된 Redis 메시지
     * @param pattern 채널 패턴 (사용하지 않음)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 수신된 메시지를 문자열로 변환
        String data = new String(message.getBody());
        System.out.println("받은 Redis Pub/Sub 이벤트: " + data);

        // 메시지를 ":" 기준으로 분할 (예: "UPDATE:department:json_data")
        String[] parts = data.split(":");

        // 메시지의 첫 번째 부분은 액션 유형 (SAVE, UPDATE, DELETE)
        String action = parts[0];

        // 두 번째 부분은 해당하는 키 값
        String key = parts[1];

        // 세 번째 부분은 변경된 값 (DELETE의 경우 값이 없을 수 있음)
        String value = parts.length > 2 ? parts[2] : null;

        // 액션 유형에 따라 처리
        switch (action) {
            case "SAVE":
            case "UPDATE":
                System.out.println("키: " + key + " 업데이트된 데이터 -> " + value);
                break;
            case "DELETE":
                System.out.println("키: " + key + " 데이터 삭제됨");
                break;
        }
    }
}
