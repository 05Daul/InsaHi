//package com.playdata.User.employee.dto;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collection;
//
//public class MyUserDetail extends User { //회사코드 추가해야하나
//
//    private UserDTO userDto;
//
//    public MyUserDetail(UserDTO userDto, Collection<? extends GrantedAuthority> authorities) {
//        super(userDto.getUserId()+"",userDto.getPassword(), authorities);
//    }
//
//    public UserDTO getUserDto() {
//        return userDto;
//    }
//}
