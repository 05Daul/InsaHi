package com.playdata.HumanResourceManagement.department.deventAPI.Service;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SendService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishDepartmentCreatedEvent(ActionBasedOrganizationChartDTO department) {
        kafkaTemplate.send("department.created", department);
    }

    public void publishDepartmentUpdatedEvent(ActionBasedOrganizationChartDTO department) {
        kafkaTemplate.send("department.updated", department);
    }

    public void publishDepartmentDeletedEvent(String companyCode, String departmentId) {
        kafkaTemplate.send("department.deleted", companyCode + ":" + departmentId);
    }
}
