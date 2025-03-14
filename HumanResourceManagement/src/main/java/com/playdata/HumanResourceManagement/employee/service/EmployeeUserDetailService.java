package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.dto.MyUserDetail;
import com.playdata.HumanResourceManagement.employee.dto.UserDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailService")
@RequiredArgsConstructor
public class EmployeeUserDetailService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    //Spring Security에서 사용자 로그인 시 자동으로 호출되는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmployeeId(username);
        if (employee == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }



        return createUser(employee);
    }




    //Spring Security의 UserDetails 객체를 생성하는 메서드
    public User createUser(Employee employee) {

        List<GrantedAuthority>authorities =
                employee.getAuthorityList().stream()
                        .map(authority ->
                                new SimpleGrantedAuthority(authority.getAuthorityName()))
                        .collect(Collectors.toList());

        UserDTO userDTO = modelMapper.map(employee, UserDTO.class);

        if (employee.getCompany() != null) {
            userDTO.setCompanyCode(employee.getCompany().getCompanyCode());
        }
        return new MyUserDetail(userDTO, authorities);
    }
}
