package com.playdata.HumanResourceManagement.department.deventAPI.dao;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class DepartmentKafkaDAO {

    private String departmentId;         // 부서 ID
    private String departmentName;       // 부서 이름
    private String action;               // 이벤트 액션 (created, updated, deleted)
    private String companyCode;          // 회사 코드 (멀티 테넌시 지원 시 필요)

    // Kafka topic 이름
    private static final String TOPIC = "department-events";

    // 생성 이벤트 생성자
    public static DepartmentKafkaDAO fromActionBasedOrganizationChartDTO(ActionBasedOrganizationChartDTO dto, String companyCode) {
        return new DepartmentKafkaDAO(
                dto.getDepartmentId(),
                dto.getDepartmentName(),
                dto.getAction(),
                companyCode // 동적으로 받은 회사 코드
        );
    }

    // 수정 이벤트 생성자
    public static DepartmentKafkaDAO updated(String departmentId, String departmentName, String companyCode) {
        return new DepartmentKafkaDAO(departmentId, departmentName, "updated", companyCode);
    }

    // 삭제 이벤트 생성자
    public static DepartmentKafkaDAO deleted(String departmentId, String departmentName, String companyCode) {
        return new DepartmentKafkaDAO(departmentId, departmentName, "deleted", companyCode);
    }

    public static DepartmentKafkaDAO created(String departmentId, String departmentName, String companyCode) {
        return new DepartmentKafkaDAO(departmentId, departmentName, "created", companyCode);
    }
}
