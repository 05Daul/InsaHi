package com.playdata.HumanResourceManagement.employee.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//UsernamePasswordAuthenticationFilter의 역할을 담당하도록 처리
//토큰의 유효성을 체크하는 작업
//토큰이 유효한 경우 -> 토큰을 꺼내서 SecurityContextHolder에 저장
//토큰이 없거나 유효하지 않은 경우  -> 컨트롤러로 요청을 넘겨서 인증을 받을 수 있도록 처리하기

//사용자정의 필터를 만들기 위해서 Filter를 구현해야 하나
//Filter를 확장한 GenericFilterBean을 상속해서 사용자정의 Filter를 구현
public class EmployeeJwtFilter extends GenericFilterBean {
    private final TokenManager tokenManager;
    public EmployeeJwtFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    //필터가 동작하면 자동으로 실행되는 메소드 - 서블릿과 라이프사이클이 동일
    //필터에서 처리할 로직을 보통 doFilter안에 정의
    //FilterChain이 실행할 필터들이 포함된 객체(서블릿도포함)
    //doFilter는 필터 체인의 일부로 동작하며, 요청이 올 때마다 실행 **
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = getToken(request); //Authorization 헤더에서 JWT 토큰을 가져옴
        // JWT 토큰이 존재하는지 확인 && 토큰 유효성 검증
        System.out.println("********************");
        System.out.println("jwt: " + jwt);
        if(StringUtils.hasText(jwt) && tokenManager.validateToken(jwt)) {
            System.out.println("인증된 사용자입니다. 인증정보를 저장합니다.");
            //유효하면 Authentication 객체를 저장
            Authentication authentication = tokenManager.getAuthentication(jwt);
            //SecurityContextHolder 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("인증되지 않은 사용자로 토큰이 없는 상태의 사용자");
        }
        System.out.println("%%%%%%%%%%%%%%%%%%");
        //필터 체인의 다음 단계(다른 필터 또는 서블릿)로 요청을 전달
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        filterChain.doFilter(servletRequest, servletResponse);

    }
    //클라이언트의 요청정보에서 토큰을 꺼내서 리턴하는 메소드
    public String getToken(HttpServletRequest request) {
        //Authorization헤더에서 토큰꺼내기
        String bearerToken = request.getHeader("Authorization");
        System.out.println("🔍 요청된 Authorization 헤더: " + bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token =bearerToken.substring(7);
            System.out.println("📌 추출된 JWT 토큰: " + token);
            return token; //"Bearer " 제거 후 JWT 리턴
        }
        return null; // 토큰이 없거나 Bearer 형식이 아닌 경우 null 리턴
    }
}
