package com.playdata.HumanResourceManagement.employee.config;

import com.playdata.HumanResourceManagement.employee.authentication.EmployeeJwtFilter;
import com.playdata.HumanResourceManagement.employee.authentication.TokenManager;
import com.playdata.HumanResourceManagement.employee.service.EmployeeUserDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenManager tokenManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/company/signup", "/employee/login", "/approavl/submit")
            .permitAll()
            .anyRequest().authenticated())
//                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new EmployeeJwtFilter(tokenManager),
                        UsernamePasswordAuthenticationFilter.class)

                //세션을 사용하지 않겠다는 의미
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );


        return http.build();
    }

}
