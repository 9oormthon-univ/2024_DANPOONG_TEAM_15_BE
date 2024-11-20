package com.ivory.ivory.service;

import com.ivory.ivory.domain.Apply;
import com.ivory.ivory.dto.KakaoPayApproveDto;
import com.ivory.ivory.dto.KakaoPayReadyDto;
import com.ivory.ivory.kakaopay.KakaoPayProperties;
import com.ivory.ivory.repository.ApplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {
    private final KakaoPayProperties properties;
    private final ApplyRepository applyRepository;
    private KakaoPayReadyDto kakaoPayReadyDto;
    private KakaoPayApproveDto kakaoPayApproveDto;

    //헤더
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + properties.getSecretKey();
        headers.set("Authorization", auth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(headers.toString());
        return headers;
    }

    //결제 준비
    public KakaoPayReadyDto kakaoPayReady(Long applyId) {
        Optional<Apply> apply = applyRepository.findById(applyId);
        if (apply.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 신청 내역입니다.");
        }
        Long totalAmount = apply.get().getTotalAmount();
        Long subsidy =  apply.get().getSubsidy();
        String copay = String.valueOf(totalAmount - subsidy);

         Map<String, Object> parameters = new HashMap<>();

        parameters.put("cid", properties.getCid());
        parameters.put("partner_order_id","ORDER_ID");
        parameters.put("partner_user_id","USER_ID");
        parameters.put("item_name","아이돌봄서비스 X 아이보리 신청");
        parameters.put("quantity","1");
        parameters.put("total_amount",copay);
        parameters.put("vat_amount","200");
        parameters.put("tax_free_amount","0");
        parameters.put("approval_url","http://localhost:3000/apply/payments/success");
        parameters.put("fail_url","http://localhost:3000/apply/payments/fail");
        parameters.put("cancel_url","http://localhost:3000/apply/payments/cancel");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());

        //외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        kakaoPayReadyDto = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoPayReadyDto.class);
        System.out.println(kakaoPayReadyDto);
        return kakaoPayReadyDto;
    }

    //결제 승인
    public KakaoPayApproveDto approveResponse(String pgToken) {
        //카카오 요청
        Map<String,Object > parameters = new HashMap<>();
        parameters.put("cid", properties.getCid());
        parameters.put("tid",kakaoPayReadyDto.getTid());
        parameters.put("partner_order_id","ORDER_ID");
        parameters.put("partner_user_id","USER_ID");
        parameters.put("pg_token",pgToken);

        //파라미터, 헤더
        HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(parameters, getHeaders());
        System.out.println(requestEntity);

        //외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        kakaoPayApproveDto = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                KakaoPayApproveDto.class);
        System.out.println(kakaoPayApproveDto);
        return kakaoPayApproveDto;
    }
}
