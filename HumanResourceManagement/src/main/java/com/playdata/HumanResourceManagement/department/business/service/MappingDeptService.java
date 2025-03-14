package com.playdata.HumanResourceManagement.department.business.service;

import com.playdata.HumanResourceManagement.department.business.dto.newDto.EmployeeDataDTO;
import com.playdata.HumanResourceManagement.department.business.dto.newDto.FullOrganizationChartDTO;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.department.business.repository.DepartmentRepository;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MappingDeptService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * 조직도 전체 조회
     */
    @Transactional(readOnly = true)
    public List<FullOrganizationChartDTO> getOrganizationChart(String companyCode) {
        List<DepartmentEntity> departments = departmentRepository.findByCompanyCode(companyCode);
        return departments.stream()
                .filter(department -> department.getParentDepartmentId() == null) // 최상위 부서 필터링
                .map(this::buildHierarchy)
                .collect(Collectors.toList());
    }

    /**
     * 부서 계층 구조 생성 (재귀)
     */
    private FullOrganizationChartDTO buildHierarchy(DepartmentEntity department) {
        List<EmployeeDataDTO> employeeDataDTOList = findEmployeesByDepartment(department.getDepartmentId());

        FullOrganizationChartDTO response = FullOrganizationChartDTO.fromEntity(department, employeeDataDTOList, null);

        department.getSubDepartments().forEach(subDept -> {
            FullOrganizationChartDTO childResponse = buildHierarchy(subDept);
            response.getSubDepartments().add(childResponse);
        });

        return response;
    }

    /**
     * 특정 부서의 직원 목록 조회
     */
    @Transactional(readOnly = true)
    public List<EmployeeDataDTO> findEmployeesByDepartment(String departmentId) {
        List<Employee> employees = employeeRepository.findByDepartment_DepartmentId(departmentId);
        return employees.stream()
                .map(EmployeeDataDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
