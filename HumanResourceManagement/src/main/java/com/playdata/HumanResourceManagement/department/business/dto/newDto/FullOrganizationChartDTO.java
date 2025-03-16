package com.playdata.HumanResourceManagement.department.business.dto.newDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FullOrganizationChartDTO implements Serializable {

    private String departmentId;       // 부서 ID
    private String departmentName;     // 부서명

    @JsonManagedReference
    private List<EmployeeDataDTO> employees;  // 직원 목록을 DTO로 변경

    @JsonManagedReference
    private List<FullOrganizationChartDTO> subDepartments = new ArrayList<>(); // 하위 부서 목록 (수정 가능한 리스트로 초기화)

    private String action;             // 액션 (예: "CREATE", "UPDATE", "DELETE")

    public static FullOrganizationChartDTO fromEntity(DepartmentEntity department, List<EmployeeDataDTO> employees, String action) {
        if (department == null) {
            throw new IllegalArgumentException("부서 정보가 없습니다.");
        }

        return FullOrganizationChartDTO.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .employees(employees != null ? employees : Collections.emptyList())
                .subDepartments(new ArrayList<>()) // 빈 리스트로 초기화
                .action(action != null ? action : "UNKNOWN")
                .build();
    }

    public void setSubDepartments(List<FullOrganizationChartDTO> subDepartments) {
        this.subDepartments = subDepartments != null ? new ArrayList<>(subDepartments) : new ArrayList<>();
    }

    public static FullOrganizationChartDTO fromJsonFormat(Object jsonObject) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            FullOrganizationChartDTO dto = objectMapper.convertValue(jsonObject, FullOrganizationChartDTO.class);

            if (jsonObject instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) jsonObject;

                if (map.containsKey("subDepartments")) {
                    List<Map<String, Object>> subDeptList = (List<Map<String, Object>>) map.get("subDepartments");
                    List<FullOrganizationChartDTO> subDepartments = subDeptList.stream()
                            .map(FullOrganizationChartDTO::fromJsonFormat)
                            .collect(Collectors.toList());
                    dto.setSubDepartments(subDepartments);
                }
            }

            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 예외 처리
        }
    }
}
