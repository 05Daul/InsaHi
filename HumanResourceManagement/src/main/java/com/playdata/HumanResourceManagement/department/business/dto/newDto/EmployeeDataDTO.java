package com.playdata.HumanResourceManagement.department.business.dto.newDto;

import com.playdata.HumanResourceManagement.employee.entity.Employee;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDataDTO {

    private String employeeId;      // 직원 ID
    private String employeeName;    // 직원 이름
    private String departmentId;    // 부서 ID
    private String status;          // 직원 상태 (Active / Inactive)
    private String position;        // 직급 (필요시 추가)

    /**
     * Entity로부터 DTO 생성
     */
    public static EmployeeDataDTO fromEntity(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("직원 정보가 없습니다.");
        }

        return EmployeeDataDTO.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getName())
                .departmentId(employee.getDepartmentId())
                .status(employee.getStatus())  // 상태 기본값 처리
                .position(employee.getPosition())  // 직급 기본값 처리
                .build();
    }
}
