package com.playdata.HumanResourceManagement.employee.NewDepartment.dto;

import com.playdata.HumanResourceManagement.department.entity.DepartmentEntity;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationStructureDTO {

    private String departmentId;                      // 부서 ID
    private String departmentName;                    // 부서명
    private String parentDepartmentId;                // 상위 부서 ID
    private int departmentLevel;                      // 부서 계층
    private List<OrganizationStructureDTO> subDepartments; // 하위 부서 목록

    /**
     * Entity → DTO 변환 메서드
     */
    public static OrganizationStructureDTO fromEntity(DepartmentEntity department) {
        if (department == null) throw new IllegalArgumentException("부서 정보가 없습니다.");

        List<OrganizationStructureDTO> subDeptList = department.getSubDepartments() != null
                ? department.getSubDepartments().stream()
                .map(OrganizationStructureDTO::fromEntity)
                .collect(Collectors.toUnmodifiableList())
                : Collections.emptyList();

        return OrganizationStructureDTO.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .parentDepartmentId(
                        department.getParentDepartmentId() != null
                                ? department.getParentDepartmentId().getDepartmentId()
                                : null
                )
                .departmentLevel(department.getDepartmentLevel())
                .subDepartments(subDeptList)
                .build();
    }
}
