//package com.playdata.HumanResourceManagement.department.deventAPI.assembler;
//
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.EmployeeDataDTO;
//import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
//import com.playdata.HumanResourceManagement.employee.entity.Employee;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//public class StreamTransition {
//
//    // FullOrganizationChartDTO를 사용하여 부서와 직원 목록을 구조화
//    public List<FullOrganizationChartDTO> assembleFullOrganizationChart(List<DepartmentEntity> departments, Map<String, List<Employee>> employeeMap) {
//        Map<String, FullOrganizationChartDTO> departmentMap = departments.stream()
//                .collect(Collectors.toMap(
//                        DepartmentEntity::getDepartmentId,
//                        dept -> FullOrganizationChartDTO.fromEntity(
//                                dept,
//                                employeeMap.getOrDefault(dept.getDepartmentId(), List.of())
//                                        .stream()
//                                        .map(EmployeeDataDTO::fromEntity)
//                                        .collect(Collectors.toList()),
//                                "CACHE_UPDATE"
//                        )
//                ));
//
//        return departments.stream()
//                .filter(dept -> dept.getParentDepartmentId() == null)  // 부모가 없는 부서만 필터
//                .map(dept -> buildFullHierarchy(dept, departmentMap))
//                .collect(Collectors.toList());
//    }
//
//    private FullOrganizationChartDTO buildFullHierarchy(DepartmentEntity department, Map<String, FullOrganizationChartDTO> departmentMap) {
//        FullOrganizationChartDTO current = departmentMap.get(department.getDepartmentId());
//        current.setSubDepartments(
//                Optional.ofNullable(department.getSubDepartments()).orElse(Collections.emptyList()).stream()
//                        .map(subDept -> buildFullHierarchy(subDept, departmentMap))
//                        .collect(Collectors.toList())
//        );
//        return current;
//    }
//
//    // OrganizationStructureDTO 사용
//    public List<OrganizationStructureDTO> assembleOrganizationStructure(List<DepartmentEntity> departments) {
//        return departments.stream()
//                .map(OrganizationStructureDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//}
