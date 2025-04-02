package com.playdata.HumanResourceManagement.employee.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.playdata.HumanResourceManagement.employee.authentication.EmployeeJwtFilter;
import com.playdata.HumanResourceManagement.employee.authentication.TokenManager;
import com.playdata.HumanResourceManagement.employee.service.EmployeeUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenManager tokenManager;
  private final EmployeeUserDetailService employeeUserDetailService;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/",
                "/company/signup",
                "/employee/login",
                "/employee/getallemployeeids",
                "/uploads/**"
//                "/employee/find",
//                "/approavl/submit",
//                "/atdsal/**",
//                "/employee/find",
//                "/company/*/start-time",
//                "/employee/*/start-time",
//                "/employee/*/company/start-time",
//                "/employee/start-Time",
//                "/employee/auth-list",
//                "/company/start-Time"
            ).permitAll()
            //관리자만 볼 수 있는 페이지 설정
            .anyRequest().authenticated())
        .addFilterBefore(new EmployeeJwtFilter(tokenManager),
            UsernamePasswordAuthenticationFilter.class)
        .userDetailsService(employeeUserDetailService)
        .formLogin(withDefaults())

        //세션을 사용하지 않겠다는 의미
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

}
