package com.ivory.ivory.service;

import com.ivory.ivory.domain.*;
import com.ivory.ivory.dto.ApplyDto;
import com.ivory.ivory.dto.ApplyListDto;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.repository.MemberRepository;
import com.ivory.ivory.repository.ApplyRepository;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final MemberRepository memberRepository;
    private final ChildRepository childRepository;
    private final ApplyRepository serviceRepository;
    private final ChildService childService;
    private final ApplyRepository applyRepository;

    public CustomApiResponse<?> applyService(ApplyDto dto, Long currentMemberId) {

        Optional<Member> member = memberRepository.findById(currentMemberId);
        if(member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }

        Optional<Child> child = childRepository.findById(dto.getChildId());

        if(child.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 자녀의 기본키 입니다.");
        }

        if(!child.get().getMember().getId().equals(currentMemberId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"본인의 자녀만 신청 가능합니다.");
        }

        //시간
        LocalDateTime startDate = dto.getStartDate();
        LocalDateTime endDate = dto.getEndDate();

        //시간으로 이용요금 계산
        Long totalAmount = calculateTotalAmount(startDate, endDate);

        //지원금
        long subsidy = 0;

        //소득 유형
        IncomeType incomeType = member.get().getIncomeType();

        //자녀 나이 계산
        LocalDate childBirthDate = child.get().getBirth();
        LocalDate nowDate = LocalDate.now();
        Long age = childService.calculateAge(childBirthDate,nowDate);

        //지원금 계산
        subsidy = getSubsidy(incomeType,age,totalAmount);

        //서비스 상태 계산
        Status status = getStatus(startDate,endDate);

        //엔티티 생성
        Apply apply = Apply.toEntity(dto,totalAmount,subsidy,incomeType,status,member.get(),child.get());
        //DB에 저장
        serviceRepository.save(apply);
        //응답 생성
        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.CREATED.value(),"서비스가 성공적으로 신청되었습니다.",null);

        return response;
    }

    //서비스 시작 시간과 종료 시갼을 매개변수로 받아 총 금액을 계산함
    public Long calculateTotalAmount(LocalDateTime startDate, LocalDateTime endDate){
        final long RATE_PER_HOUR = 13900;
        long hours = Duration.between(startDate, endDate).toHours(); // 이용 시간 계산
        // 총 이용 금액
        return hours * RATE_PER_HOUR;
    }

    //지원금 계산
    public Long getSubsidy(IncomeType incomeType,Long age, Long totalAmount) {
        if (IncomeType.A.equals(incomeType)) {
            if (age < 8) { // 미취학 아동
                return (long) (totalAmount * 0.85); // 85% 지원
            }
            else { // 취학 아동
                return (long) (totalAmount * 0.75); // 75% 지원
            }
        }
        else if (IncomeType.B.equals(incomeType)) {
            if (age < 8) { // 미취학 아동
                return (long) (totalAmount * 0.60); // 60% 지원
            }
            else { // 취학 아동
                return (long) (totalAmount * 0.50); // 50% 지원
            }
        }
        else if (IncomeType.C.equals(incomeType) || IncomeType.D.equals(incomeType)) {
            return (long) (totalAmount * 0.50); // 50% 지원
        }
        else {
            throw new IllegalArgumentException("올바르지 않은 소득 유형입니다.");
        }
    }

    //서비스 상태 계산
    public Status getStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            return Status.YET;
        } else if (!now.isAfter(endDate)) {
            return Status.IN_PROGRESS;
        } else {
            return Status.COMPLETE;
        }
    }

    public CustomApiResponse<?> getApplyList(Long childId, Long currentMemberId) {
        //유저 검증
        Optional<Member> member = memberRepository.findById(currentMemberId);
        if(member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 멤버입니다.");
        }
        //자녀 검증
        Optional<Child> child = childRepository.findById(childId);

        // 자녀 정보가 없을 경우 예외 처리
        if (child.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 자녀의 기본키 입니다.");
        }

        //조회한 유저의 자녀가 아닌 경우의 신청 내역은 조회하지 못하도록 에러 처리
        if (!child.get().getMember().getId().equals(currentMemberId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인 자녀의 신청 내역만 조회할 수 있습니다.");
        }

        List<Apply> applyList = applyRepository.findAllByChild_Id(child.get().getId());

        //응답 dto 생성
        List<ApplyListDto> applyListDto = new ArrayList<>();
        applyList.forEach(apply -> {

            //신청날짜 데이터 포맷
            String applyDate = apply.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

            //서비스 이용 시간 데이터 포맷
            String startTime = apply.getStartDate().format(DateTimeFormatter.ofPattern("HH:mm"));
            String endTime = apply.getEndDate().format(DateTimeFormatter.ofPattern("HH:mm"));
            String careTime = startTime + " ~ " + endTime;

            //서비스 상태
            Status serviceStatus = getStatus(apply.getStartDate(),apply.getEndDate());
            String status = "";
            switch (serviceStatus) {
                case YET: status =  "서비스 신청 완료"; break;
                case IN_PROGRESS: status = "돌봄 서비스 이용 중"; break;
                case COMPLETE: status = "이용 완료"; break;
            }

            applyListDto.add(ApplyListDto.from(applyDate,careTime,status));
        });

        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"신청 목록 조회에 성공했습니다.",applyListDto);
    }
}
