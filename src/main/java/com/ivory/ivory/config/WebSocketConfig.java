package com.ivory.ivory.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 엔드포인트
        registry.enableSimpleBroker("/topic");
        // 클라이언트가 메시지를 보낼 엔드포인트
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결 엔드포인트
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "*"
                );
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                        "https://ivorygroup.click",
                        "https://danpoong-ivory.vercel.app",
                        "http://localhost:3000"
                )
                .withSockJS(); // SockJS 지원
    }

}