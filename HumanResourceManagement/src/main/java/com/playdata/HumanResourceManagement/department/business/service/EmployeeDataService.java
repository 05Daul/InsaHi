package com.playdata.HumanResourceManagement.department.business.service;

import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.department.business.repository.DepartmentRepository;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeDataService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void transferEmployee(String employeeId, String newDepartmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("직원을 찾을 수 없습니다."));

        DepartmentEntity newDepartment = departmentRepository.findById(newDepartmentId)
                .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다."));

        employee.changeDepartment(newDepartment);  // 부서 변경
        employeeRepository.save(employee); // 변경된 직원 정보 저장
    }
}
