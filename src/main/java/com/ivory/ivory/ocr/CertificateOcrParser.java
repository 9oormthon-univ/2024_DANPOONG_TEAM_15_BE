package com.ivory.ivory.ocr;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component()
@Slf4j
public class CertificateOcrParser implements OcrParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> parse(String response) {
        Map<String, String> result = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(response);

            // 첫 번째 이미지 데이터 추출
            JsonNode image = root.path("images").get(0);
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("OCR 응답에서 이미지 데이터가 없습니다.");
            }

            // "fields" 배열에서 각 필드 추출
            JsonNode fields = image.path("fields");
            for (JsonNode field : fields) {
                String name = field.path("name").asText();
                String value = field.path("inferText").asText();
                result.put(name, value);
            }
        } catch (Exception e) {
            log.error("OCR 응답 파싱 중 오류 발생", e);
            throw new RuntimeException("OCR 응답 파싱 실패", e);
        }
        return result;
    }
}