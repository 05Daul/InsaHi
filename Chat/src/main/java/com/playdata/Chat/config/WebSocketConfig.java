package com.playdata.Chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    /**
     * STOMP 엔드포인트 등록
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("✅ WebSocket STOMP 엔드포인트 등록됨: /ws-stomp");
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                //클라이언트의 도메인이나 포트에 상관없이 WebSocket 연결 요청을 허용
                .withSockJS();
    }

    /**
     * 메시지 브로커 설정
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트 구독 prefix: /topic, /queue
        registry.enableSimpleBroker("/topic", "/queue");
        // 클라이언트 발행 prefix: /app
        registry.setApplicationDestinationPrefixes("/app");
    }
}
