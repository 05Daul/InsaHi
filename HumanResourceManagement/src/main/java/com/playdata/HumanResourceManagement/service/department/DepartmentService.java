package com.playdata.HumanResourceManagement.service.department;

import com.playdata.HumanResourceManagement.entity.Department;
import com.playdata.HumanResourceManagement.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // 1. 부서 업데이트
    public Department updateDepartment(String departmentId, Department updatedDepartment) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("부서가 존재하지 않습니다."));  // 예외 메시지 개선
        department.setDepartmentName(updatedDepartment.getDepartmentName());
        department.setParentDepartmentId(updatedDepartment.getParentDepartmentId());
        department.setDepartmentLevel(updatedDepartment.getDepartmentLevel());
        department.setCompanyCode(updatedDepartment.getCompanyCode());
        department.setLeft(updatedDepartment.getLeft());
        department.setRight(updatedDepartment.getRight());
        return departmentRepository.save(department);
    }

    // 2. 부서 삭제
    public void deleteDepartment(String departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("부서가 존재하지 않습니다.");  // 예외 처리 개선
        }
        departmentRepository.deleteById(departmentId);
    }

    // 3. 특정 부서 조회
    public Optional<Department> getDepartmentById(String departmentId) {
        return departmentRepository.findById(departmentId);
    }

    // 4. 회사 코드로 모든 부서 조회
    public List<Department> getDepartmentsByCompanyCode(String companyCode) {
        return departmentRepository.findByCompanyCode(companyCode);
    }

    // 5. 회사 코드와 부서 계층으로 부서 조회
    public List<Department> getDepartmentsByLevel(String companyCode, int departmentLevel) {
        return departmentRepository.findByCompanyCodeAndDepartmentLevel(companyCode, departmentLevel);
    }
}
