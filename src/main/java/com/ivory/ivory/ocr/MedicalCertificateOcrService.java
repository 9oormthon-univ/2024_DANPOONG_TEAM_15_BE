package com.ivory.ivory.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalCertificateOcrService {
    public String processOcr(MultipartFile file, String apiUrl, String secretKey) throws IOException {
        // 1. JSON 메시지 생성
        String jsonMessage = createJsonMessage(file);

        // 2. HTTP 연결 설정
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("x-incizorlens-api-key", secretKey);
        connection.setReadTimeout(30000);

        // 3. JSON 데이터 전송
        try (var outputStream = connection.getOutputStream()) {
            outputStream.write(jsonMessage.getBytes("UTF-8"));
            outputStream.flush();
        }

        // 4. 응답 처리
        int responseCode = connection.getResponseCode();
        System.out.println("responseCode = " + responseCode);
        System.out.println("responseMessage = " + connection.getResponseMessage());
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("OCR 요청 실패: 상태 코드 = " + responseCode + connection.getResponseMessage());
        }

        // 5. 응답 데이터 읽기
        return new String(connection.getInputStream().readAllBytes(), "UTF-8");
    }

    private String createJsonMessage(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 메시지 생성
        ObjectNode root = objectMapper.createObjectNode();
        root.put("version", "V2");
        root.put("requestId", UUID.randomUUID().toString());
        root.put("timestamp", System.currentTimeMillis());

        // 이미지 정보 추가
        ArrayNode images = objectMapper.createArrayNode();
        ObjectNode image = objectMapper.createObjectNode();
        image.put("format", "jpg");
        image.put("name", file.getName());

        // 파일 데이터 Base64 인코딩
        String base64File = Base64.getEncoder().encodeToString(file.getBytes());
        image.put("data", base64File); // Base64 데이터를 JSON에 추가
        images.add(image);

        root.set("images", images);

        return objectMapper.writeValueAsString(root);
    }
}