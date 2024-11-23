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
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://danpoong-ivory.vercel.app/");
        config.addAllowedOrigin("https://ivorygroup.click");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용

        source.registerCorsConfiguration("/**", config); // 모든 엔드포인트에 대해 CORS 허용
        return new CorsFilter(source);
    }
}
