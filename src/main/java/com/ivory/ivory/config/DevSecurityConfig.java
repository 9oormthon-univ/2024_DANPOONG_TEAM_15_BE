package com.ivory.ivory.config;

import com.ivory.ivory.jwt.JwtAccessDeniedHandler;
import com.ivory.ivory.jwt.JwtAuthenticationEntryPoint;
import com.ivory.ivory.jwt.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Profile({"dev", "local"})
public class DevSecurityConfig {
    private final JwtSecurityConfig jwtSecurityConfig;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // exception handling 설정
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // 헤더 설정
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))

                // 세션을 Stateless 로 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인증/인가 설정
                .authorizeHttpRequests(auth -> auth
                        // Swagger 경로 예외 처리
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 인증 없이 허용할 추가 경로
                        .requestMatchers("/auth/**").permitAll()

                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        // JWT Security 설정 적용
        jwtSecurityConfig.configure(http);

        return http.build();
    }
}