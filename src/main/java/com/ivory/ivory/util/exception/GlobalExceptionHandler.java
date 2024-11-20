package com.ivory.ivory.util.exception;

import com.ivory.ivory.util.response.CustomApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

// 전역 예외 처리를 위한 클래스
@ControllerAdvice
public class GlobalExceptionHandler {
    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomApiResponse<?>> handleResponseStatusException(ResponseStatusException ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(ex.getStatusCode().value(), ex.getReason());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 유효성 검증 실패 에러 메시지를 Map에 저장
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // CustomApiResponse에 메시지 전달
        CustomApiResponse<?> response = CustomApiResponse.createFailWithData(
                HttpStatus.BAD_REQUEST.value(),
                "입력값이 올바르지 않습니다.",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // AuthenticationException 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomApiResponse<?>> handleAuthenticationException(AuthenticationException ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.UNAUTHORIZED.value(), "인증 실패: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


    // 모든 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<?>> handleException(Exception ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

