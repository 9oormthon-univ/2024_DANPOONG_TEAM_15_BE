package com.ivory.ivory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("")
    public ResponseEntity<String> testApi() {
        String result = "API 통신에 성공하였습니다.";
        return ResponseEntity.ok(result);
    }
}
