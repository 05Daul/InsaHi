package com.playdata.HumanResourceManagement.department.business.dto.newDto;

import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActionBasedOrganizationChartDTO {

    private String departmentId;   // 부서 ID
    private String departmentName; // 부서명
    private String action;         // 액션 (예: "CREATE", "UPDATE", "DELETE")

    /**
     * Entity → DTO 변환 메서드
     */
    public static ActionBasedOrganizationChartDTO fromDepartment(DepartmentEntity department, String action) {
        if (department == null) throw new IllegalArgumentException("부서 정보가 없습니다.");

        return ActionBasedOrganizationChartDTO.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .action(action != null ? action : "UNKNOWN")
                .build();
    }
}
