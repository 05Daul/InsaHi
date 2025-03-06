package com.playdata.attendanceSalary.atdSalDao.atd;

import com.playdata.User.company.entity.Company;
import com.playdata.User.company.repository.CompanyRepository;
import com.playdata.User.employee.entity.Employee;
import com.playdata.User.employee.repository.EmployeeRepository;
import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.attendanceSalary.atdSalRepository.atd.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class AttendanceDAOImpl implements AttendanceDAO{

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    @Override
    public Company findCompanyById(String id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public Employee findEmployeeById(String employeeid) {
        return employeeRepository.findById(employeeid).orElse(null);
    }


    @Override
    public AttendanceEntity findById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    @Override
    public AttendanceEntity save(AttendanceEntity attendance) {
        return attendanceRepository.save(attendance);
    }


    /**
    @Override // 외근 추가
    public AttendanceEntity findWorkingOutside() {
        return attendanceRepository.findWorkingOutside();
    }
    */
}

