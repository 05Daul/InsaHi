package com.playdata.User.employee.dto;



import com.playdata.User.employee.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

//엔티티 대신 각 레이어로 전달될 데이터클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String employeeId;
    private String password;
    private String companyCode;
    private String name;
    private Set<Authority> authoritylist;

}
