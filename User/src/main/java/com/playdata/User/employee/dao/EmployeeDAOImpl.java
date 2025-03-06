package com.playdata.User.employee.dao;


import com.playdata.User.employee.entity.Employee;
import com.playdata.User.employee.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeDAOImpl implements EmployeeDAO {

    private final EmployeeRepository employeeRepository;

    public void insert(Employee employee) {
        employeeRepository.save(employee);
    }
}
