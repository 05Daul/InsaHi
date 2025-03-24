package com.playdata.HumanResourceManagement.employee.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MyUserDetail extends User {

    private UserDTO userDto;

    public MyUserDetail(UserDTO userDto, Collection<? extends GrantedAuthority> authorities) {
        super(userDto.getEmployeeId()+"     ",userDto.getPassword(), authorities);
        this.userDto = userDto;
    }

    public UserDTO getUserDto() {
        return userDto;
    }

    // 회사 코드 반환
    public String getCompanyCode() {
        return userDto.getCompanyCode();
    }
}
