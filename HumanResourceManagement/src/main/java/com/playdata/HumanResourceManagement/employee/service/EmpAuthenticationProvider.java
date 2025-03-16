package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.authentication.EmpAuthenticationToken;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.dto.MyUserDetail;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class EmpAuthenticationProvider implements AuthenticationProvider {

    private final EmployeeUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 1️⃣ 인증 객체가 올바른 타입인지 확인
//        if (authentication instanceof EmpAuthenticationToken) {
//
//            EmpAuthenticationToken authToken = (EmpAuthenticationToken) authentication;
//            String employeeId = authToken.getName();
//            String password = authToken.getCredentials().toString();
//            String companyCode = authToken.getCompanyCode();
//
//            // 2️⃣ 사용자 정보 조회
//            MyUserDetail userDetail = (MyUserDetail) userDetailService.loadUserByUsername(employeeId);
//            if (userDetail != null) {
//                // 3️⃣ 비밀번호 검증
//                if (passwordEncoder.matches(password, userDetail.getPassword())) {
//                    // 4️⃣ 회사 코드 검증
//                    if (userDetail.getUserDto().getCompanyCode().equals(companyCode)) {
//                        // 5️⃣ 모든 검증 통과 -> 로그인 성공
//                        LoginDTO loginDTO = modelMapper.map(userDetail, LoginDTO.class);
//                        return new EmpAuthenticationToken(loginDTO, null, companyCode, userDetail.getAuthorities());
//                    } else {
//                        throw new AuthenticationException("잘못된 회사 코드입니다.") {};
//                    }
//                } else {
//                    throw new AuthenticationException("잘못된 비밀번호입니다.") {};
//                }
//            } else {
//                throw new AuthenticationException("사용자를 찾을 수 없습니다.") {};
//            }
//        } else {
//            throw new AuthenticationException("잘못된 인증 객체입니다.") {};
//        }
//
//    }
        //1. 인증 객체가 올바른 타입인지
        if (!(authentication instanceof EmpAuthenticationToken)) {
            throw new AuthenticationException("잘못된 인증 객체입니다.") {
            };
        }

        EmpAuthenticationToken authToken = (EmpAuthenticationToken) authentication;
        String employeeId = authToken.getName();
        String password = authToken.getCredentials().toString();
        String companyCode = authToken.getCompanyCode();

        // 사용자 정보 조회
        MyUserDetail userDetail = (MyUserDetail) userDetailService.loadUserByUsername(employeeId);
        if (userDetail == null) {
            throw new AuthenticationException("사용자를 찾을 수 없습니다.") {
            };
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, userDetail.getPassword())) {
            throw new AuthenticationException("잘못된 비밀번호입니다.") {
            };
        }

        // 회사 코드 검증
        if (!userDetail.getUserDto().getCompanyCode().equals(companyCode)) {
            throw new AuthenticationException("잘못된 회사 코드입니다.") {
            };
        }

        //모든 검증 통과 -> 로그인 성공
        LoginDTO loginDTO = modelMapper.map(userDetail, LoginDTO.class);
        return new EmpAuthenticationToken(loginDTO, null, companyCode, userDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}