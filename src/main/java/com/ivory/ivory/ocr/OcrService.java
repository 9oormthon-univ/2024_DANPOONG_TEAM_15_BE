package com.ivory.ivory.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrService {
    @Value("${ocr.api.url}")
    private String apiUrl;
    @Value("${ocr.api.secret-key}")
    private String secretKey;
//    @Value("#{'${ocr.template-ids}'.split(',')}")
//    private List<String> templateIds;

    public String processOcr(MultipartFile file) throws IOException {
        // 1. JSON 메시지 생성
        String jsonMessage = createJsonMessage();

        // 2. HTTP 연결 설정
        String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("X-OCR-SECRET", secretKey);
        connection.setReadTimeout(30000);

        // 3. 멀티파트 데이터 작성
        try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            writeMultipartData(outputStream, jsonMessage, file, boundary);
        }

        // 4. 응답 처리
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("OCR 요청 실패: 상태 코드 = " + responseCode + connection.getResponseMessage());
        }

        // 5. 응답 데이터 읽기
        return new String(connection.getInputStream().readAllBytes());
    }

    private String createJsonMessage() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 메시지 생성
        ObjectNode root = objectMapper.createObjectNode();
        root.put("version", "V2");
        root.put("requestId", UUID.randomUUID().toString());
        root.put("timestamp", System.currentTimeMillis());

        // 이미지 정보 추가
        ArrayNode images = objectMapper.createArrayNode();
        ObjectNode image = objectMapper.createObjectNode();
//        ArrayNode templateIdsNode = objectMapper.createArrayNode();
        image.put("format", "jpg");
        image.put("name", "uploaded_image");
//        image.put("templateIds", objectMapper.createArrayNode());
//        System.out.println(templateIds);
//        for (String templateId : templateIds) {
//            templateIdsNode.add(templateId);
//        }
        images.add(image);

        root.set("images", images);

        return objectMapper.writeValueAsString(root);
    }

    private void writeMultipartData(DataOutputStream outputStream, String jsonMessage, MultipartFile file, String boundary) throws IOException {
        // JSON 메시지 작성
        outputStream.writeBytes("--" + boundary + "\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"message\"\r\n\r\n");
        outputStream.writeBytes(jsonMessage);
        outputStream.writeBytes("\r\n");

        // 파일 데이터 작성
        outputStream.writeBytes("--" + boundary + "\r\n");
        outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n");
        outputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
        outputStream.write(file.getBytes());
        outputStream.writeBytes("\r\n");

        // 끝나는 바운더리 작성
        outputStream.writeBytes("--" + boundary + "--\r\n");
        outputStream.flush();
    }
}