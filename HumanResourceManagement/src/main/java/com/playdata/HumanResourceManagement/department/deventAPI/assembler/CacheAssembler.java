//package com.playdata.HumanResourceManagement.department.deventAPI.assembler;
//
//import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
//import com.playdata.HumanResourceManagement.department.business.service.MappingDeptService;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.ActionBasedOrganizationChartDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.OrganizationStructureDTO;
//import com.playdata.HumanResourceManagement.department.business.dto.newDto.EmployeeDataDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class CacheAssembler {
//
//    private final MappingDeptService mappingDeptService;
//
//    /**
//     * Redis 키 생성
//     */
//    public String buildOrganizationChartKey(String companyCode) {
//        return "organizationChart::" + companyCode;
//    }
//
//    /**
//     * FullOrganizationChartDTO 변환
//     */
//    public List<FullOrganizationChartDTO> toCacheFormat(List<DepartmentEntity> departments) {
//        return departments.stream()
//                .map(department -> {
//                    List<EmployeeDataDTO> employees = mappingDeptService.findEmployeesByDepartment(department.getDepartmentId());
//                    return FullOrganizationChartDTO.fromEntity(department, employees, "CACHE_UPDATE");
//                })
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * ActionBasedOrganizationChartDTO 변환
//     */
//    public List<ActionBasedOrganizationChartDTO> toActionBasedCacheFormat(List<DepartmentEntity> departments, String action) {
//        return departments.stream()
//                .map(department -> ActionBasedOrganizationChartDTO.fromDepartment(department, action))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * OrganizationStructureDTO 변환
//     */
//    public List<OrganizationStructureDTO> toStructureCacheFormat(List<DepartmentEntity> departments) {
//        return departments.stream()
//                .map(OrganizationStructureDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * EmployeeDataDTO 변환
//     */
//    public List<EmployeeDataDTO> toEmployeeCacheFormat(List<EmployeeDataDTO> employees) {
//        return employees; // 변환 없이 그대로 반환
//    }
//}
