package com.ivory.ivory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 인증정보 허용
        config.setAllowCredentials(false); // 일단 이렇게..
        //config.addAllowedOrigin("http://localhost:3000"); // React 애플리케이션 URL
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용

        source.registerCorsConfiguration("/**", config); // 모든 엔드포인트에 대해 CORS 허용
        return new CorsFilter(source);
    }
}
