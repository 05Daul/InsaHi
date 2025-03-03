package com.playdata.HumanResourceManagement.service.human;

import com.playdata.HumanResourceManagement.entity.Employee;
import com.playdata.HumanResourceManagement.repository.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;         // List 임포트
import java.util.Map;          // Map 임포트
import java.util.Optional;     // Optional 임포트

@Service
public class HumanService {

    @Autowired
    private HumanRepository humanRepository;

    // 회사 코드로 직원 목록 조회
    public Map<String, Object> getEmployeeListByCompanyCode(String companyCode) {
        List<Employee> employees = humanRepository.getEmployeesByCompany_CompanyCode(companyCode);
        return Map.of("message", "직원 목록 조회 성공", "companyCode", companyCode, "employees", employees);
    }

    // 🆕 직원 상세 조회
    public Map<String, Object> getEmployeeDetail(String companyCode, String employeeId) {
        Employee employee = humanRepository.getEmployeeByCompany_CompanyCodeAndEmployeeId(companyCode, employeeId);
        if (employee != null) {
            return Map.of("message", "직원 상세 조회 성공", "employee", employee);
        } else {
            throw new RuntimeException("해당 직원 정보를 찾을 수 없습니다.");
        }
    }

    // 직원 권한 변경
    public void changeUserRole(String id, Employee.Role newRole) {
        Optional<Employee> optionalEmployee = humanRepository.findById(Integer.parseInt(id));
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setRole(newRole);
            humanRepository.save(employee);
        } else {
            throw new RuntimeException("직원을 찾을 수 없습니다.");
        }
    }

    // 직원 추가
    public void addEmployee(Map<String, String> userDetails) {
        Employee employee = new Employee();
        employee.setName(userDetails.get("name"));
        employee.setEmail(userDetails.get("email"));
        employee.setRole(Employee.Role.valueOf(userDetails.get("role")));
        humanRepository.save(employee);
    }

    // 직원 삭제
    public void deleteEmployee(String companyCode, String employeeId) {
        if (humanRepository.getEmployeeByCompany_CompanyCodeAndEmployeeId(companyCode, employeeId) != null) {
            humanRepository.removeEmployeeByCompany_CompanyCodeAndEmployeeId(companyCode, employeeId);
        } else {
            throw new RuntimeException("직원을 찾을 수 없습니다.");
        }
    }

    // 직원 수정
    public void updateEmployee(String id, Map<String, String> updatedDetails) {
        int employeeId = Integer.parseInt(id);
        Optional<Employee> optionalEmployee = humanRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (updatedDetails.containsKey("name")) {
                employee.setName(updatedDetails.get("name"));
            }
            if (updatedDetails.containsKey("email")) {
                employee.setEmail(updatedDetails.get("email"));
            }
            if (updatedDetails.containsKey("role")) {
                employee.setRole(Employee.Role.valueOf(updatedDetails.get("role")));
            }
            humanRepository.save(employee);
        } else {
            throw new RuntimeException("직원을 찾을 수 없습니다.");
        }
    }
}
