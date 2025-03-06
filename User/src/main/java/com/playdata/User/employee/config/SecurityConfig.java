package com.playdata.User.employee.config;

import com.playdata.User.employee.authentication.EmployeeJwtFilter;
import com.playdata.User.employee.authentication.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }344

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer :: disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/company/signup").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new EmployeeJwtFilter(tokenManager),
                        UsernamePasswordAuthenticationFilter.class)

                //세션을 사용하지 않겠다는 의미
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) );


        return http.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        //*은 모든 도메인에 대해서 응답을 허용
//        //http://127.0.0.1:3000에 대해서만 응답을 허용
//        //json서버응답을 자바스크립트에서 처리할 수 있도록 허용
//        //메소드에 대한 허용,모든 http헤더에대해서 ㅎ서용
//        //config에는 자원에 대한 허용범위
//        configuration.addAllowedOrigin("http://127.0.0.1:3000");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//        configuration.setMaxAge(3600L);//preflight요청결과를 캐싱
//        //config셋팅한걸 어떤 요청에 적용할 것인지 설정
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
