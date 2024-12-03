package com.ivory.ivory.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
public class MedicalCertificateOcrParser implements OcrParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> parse(String response) {
        Map<String, String> result = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(response);

            // 첫 번째 이미지 데이터 추출
            JsonNode image = root.path("result").path("images").get(0);
            if (image == null || image.isEmpty() || !image.has("result")) {
                throw new IllegalArgumentException("OCR 응답에서 이미지 데이터가 없습니다.", null);
            }

            // "cl" 배열에서 각 필드 추출
            JsonNode fields = image.path("result").path("cl");
            if (fields == null || !fields.isArray()) {
                throw new IllegalArgumentException("OCR 응답에서 필드 데이터가 없습니다.", null);
            }

            for (JsonNode field : fields) {
                String name = field.path("category").asText(null);
                String value = field.path("value").asText(null);

                if (name == null || value == null) {
                    log.warn("필드 데이터가 누락되었습니다: {}", field);
                    continue;
                }

                result.put(name, value);
            }

            if (result.isEmpty()) {
                throw new IllegalArgumentException("OCR 결과에 필수 필드가 없습니다.", null);
            }
        } catch (Exception e) {
            log.error("OCR 응답 파싱 실패: 응답 내용 = {}", response, e);
            throw new RuntimeException("OCR 응답 파싱 실패", e);
        }
        return result;
    }
}