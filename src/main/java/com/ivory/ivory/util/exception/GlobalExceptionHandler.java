package com.ivory.ivory.util.exception;

import com.ivory.ivory.util.response.CustomApiResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 모든 기타 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<?>> handleException(Exception ex) {
        CustomApiResponse<?> response = CustomApiResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

