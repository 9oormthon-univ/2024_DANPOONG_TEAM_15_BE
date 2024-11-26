package com.ivory.ivory.service;

import com.ivory.ivory.domain.Apply;
import com.ivory.ivory.domain.Authority;
import com.ivory.ivory.domain.Caregiver;
import com.ivory.ivory.dto.ApplyListDto;
import com.ivory.ivory.dto.CareListDto;
import com.ivory.ivory.repository.ApplyRepository;
import com.ivory.ivory.repository.CaregiverRepository;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaregiverService {

    private final ApplyRepository applyRepository;
    private final ApplyService applyService;
    private final CaregiverRepository caregiverRepository;

    public CustomApiResponse<?> getCareList(Long currentMemberId) {
        List<Apply> applyList = applyRepository.findAll();
        List<CareListDto> careListDto = new ArrayList<>();
        applyList.forEach(apply -> {
            //서비스 신청 날짜
            String applyDate = applyService.getApplyDate(apply.getCreateAt());

            //서비스 이용 날짜
            String careDate = apply.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            //서비스 이용 시간
            String careTime = applyService.getCareTime(apply.getStartDate(),apply.getEndDate());

            careListDto.add(CareListDto.toCareList(applyDate,careDate,careTime));
        });
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"돌봄 활동 목록이 조회되었습니다.",careListDto);
    }
}
