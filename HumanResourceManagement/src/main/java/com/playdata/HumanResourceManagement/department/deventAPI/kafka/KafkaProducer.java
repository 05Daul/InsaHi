package com.playdata.HumanResourceManagement.department.deventAPI.kafka;

import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, DepartmentEntity> kafkaTemplate;

    /**
     * 부서 생성 이벤트 Kafka 발행 (비동기)
     */
    @Async
    public void publishDepartmentCreatedEventAsync(DepartmentEntity department) {
        try {
            kafkaTemplate.send("department-events", department);
            log.info("Kafka 이벤트 발행 완료: {}", department);
        } catch (Exception e) {
            log.error("Kafka 이벤트 발행 실패: {}", department, e);
        }
    }
}
