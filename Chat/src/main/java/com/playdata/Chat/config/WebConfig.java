package com.playdata.Chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 실제 물리 폴더 경로
        String fullPath = System.getProperty("user.dir") + "/uploads/";
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + fullPath);  // ✅ 서버 내부 폴더를 정적 리소스로 제공
    }
}
