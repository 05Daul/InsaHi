package com.playdata.HumanResourceManagement.department.deventAPI.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.deventAPI.redis.RedisService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@Service
public class KafkaConsumerService {
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(RedisService redisService, ObjectMapper objectMapper) {
        this.redisService = redisService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "organization-updates", groupId = "org-group")
    public void listenOrganizationUpdate(ConsumerRecord<String, String> record) {
        System.out.println("📩 Kafka 이벤트 수신: " + record.value());

        try {
            // ✅ Kafka 메시지 로깅 (디버깅용)
            System.out.println("📩 Kafka Raw 메시지: " + record.value());

            // ✅ JSON 변환 (ActionBasedOrganizationChartDTO)
            ActionBasedOrganizationChartDTO actionDTO = objectMapper.readValue(record.value(), ActionBasedOrganizationChartDTO.class);

            // ✅ 필수 필드 검증
            if (Stream.of(actionDTO, actionDTO.getAction(), actionDTO.getDepartmentId()).anyMatch(x -> x == null)) {
                System.err.println("⚠️ 잘못된 Kafka 메시지: 필수 필드 누락!");
                return;
            }

            // ✅ 회사 코드 설정
            String companyCode = (record.key() != null) ? record.key() : "UNKNOWN";

            // ✅ 액션별 동작을 `Map`에 저장
            Map<String, BiConsumer<String, ActionBasedOrganizationChartDTO>> actionHandlers = Map.of(
                    "DELETE", (code, res) -> {
                        redisService.deleteOrganizationChart(code);
                        System.out.println("🗑 조직도 삭제 완료 | 회사 코드: " + code);
                    },
                    "UPDATE", (code, res) -> {
                        redisService.updateActionBasedOrganizationChart(code, res);
                        System.out.println("✅ 조직도 업데이트 완료 | 회사 코드: " + code);
                    },
                    "CREATE", (code, res) -> {
                        redisService.updateActionBasedOrganizationChart(code, res);
                        System.out.println("✅ 조직도 생성 완료 | 회사 코드: " + code);
                    }
            );

            // ✅ 액션 실행 (미정의 액션 예외 처리)
            actionHandlers
                    .getOrDefault(actionDTO.getAction().toUpperCase(), (code, res) ->
                            System.out.println("⚠️ 알 수 없는 액션: " + res.getAction())
                    ).accept(companyCode, actionDTO);

        } catch (JsonProcessingException e) {
            System.err.println("❌ Kafka 메시지 파싱 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("🚨 예기치 못한 오류 발생: " + e.getMessage());
        }
    }
}
