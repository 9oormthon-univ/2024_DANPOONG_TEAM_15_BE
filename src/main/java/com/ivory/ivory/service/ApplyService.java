package com.ivory.ivory.service;

import com.ivory.ivory.domain.*;
import com.ivory.ivory.dto.ApplyDetailDto;
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
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//TODO : 진단서, 미등원 확인서 등록 서류 추가 필요
public class ApplyService {
    private final MemberRepository memberRepository;
    private final ChildRepository childRepository;
    private final ApplyRepository serviceRepository;
    private final ChildService childService;
    private final ApplyRepository applyRepository;

    //서비스 신청
    public CustomApiResponse<?> applyService(ApplyDto dto, Long currentMemberId) {
        //유저 검증
        Optional<Member> member = memberRepository.findById(currentMemberId);
        if(member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
        }

        Optional<Child> child = childRepository.findById(dto.getChildId());
        if(child.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 자녀의 기본키 입니다.");
        }
        //조회한 유저의 자녀가 아닌 경우의 신청 하지 못하도록 에러 처리
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
        Status status = getNowStatus(startDate,endDate);

        //엔티티 생성
        Apply apply = Apply.toEntity(dto,totalAmount,subsidy,incomeType,status,member.get(),child.get());
        //DB에 저장
        serviceRepository.save(apply);
        //응답 생성
        CustomApiResponse<?> response = CustomApiResponse.createSuccess(HttpStatus.CREATED.value(),"서비스가 성공적으로 신청되었습니다.",null);

        return response;
    }

    //신청 목록 조회
    public CustomApiResponse<?> getApplyList(Long childId, Long currentMemberId) {
        //유저 검증
        Optional<Member> member = memberRepository.findById(currentMemberId);
        if(member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 멤버입니다.");
        }

        Optional<Child> child = childRepository.findById(childId);
        //자녀 정보가 없을 경우 예외 처리
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

            //서비스 이용 날짜 (0000년 00월 00일)
            String applyDate = getApplyDate(apply.getStartDate());

            //서비스 이용 시간 (00:00 ~ 00:00)
            String careTime = getCareTime(apply.getStartDate(),apply.getEndDate());

            //호출 시점의 서비스 상태 조회
            Status serviceStatus = getNowStatus(apply.getStartDate(),apply.getEndDate());
            String status = getStatus(serviceStatus);

            applyListDto.add(ApplyListDto.from(applyDate,careTime,status));
        });
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"신청 목록 조회에 성공했습니다.",applyListDto);
    }

    //신청 세부 조회
    public CustomApiResponse<?> getApplyDetail(Long applyId, Long currentMemberId) {

        //유저 검증
        Optional<Member> member = memberRepository.findById(currentMemberId);
        if(member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"존재하지 않는 멤버입니다.");
        }

        Optional<Apply> apply = applyRepository.findById(applyId);
        if (apply.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 신청 내역입니다.");
        }
        if(!apply.get().getMember().getId().equals(currentMemberId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"조회할 권한이 없는 신청내역입니다.");
        }

        Optional<Child> child = childRepository.findById(apply.get().getChild().getId());

        //신청 날짜
        String applyDate = getApplyDate(apply.get().getCreateAt());

        //자녀 이름
        String name = child.get().getName();

        //자녀 생년 월일
        LocalDate birthDate = child.get().getBirth();

        //자녀의 나이 계산
        Long age = childService.calculateAge(birthDate,LocalDate.now());

        //소득 유형
        IncomeType incomeType = member.get().getIncomeType();

        LocalDateTime startDate = apply.get().getStartDate();
        LocalDateTime endDate = apply.get().getEndDate();

        //서비스 이용날짜
        String careDate = getApplyDate(startDate);

        //서비스 이용시간
        String careTime = getCareTime(startDate,endDate);

        //기본 요금
        Long totalAmount = calculateTotalAmount(startDate,endDate);

        //지원 요금
        Long subsidy = getSubsidy(incomeType,age,totalAmount);

        //본인 부담금
        Long copay = totalAmount - subsidy;

        //호출 시점의 상태
        Status nowStatus = getNowStatus(startDate,endDate);
        String status = getStatus(nowStatus);

        //응답 Dto 생성
        ApplyDetailDto applyDetailDto = ApplyDetailDto.from(
                applyDate,
                name,
                birthDate,
                age,
                incomeType.getDescription(),
                careDate,
                careTime,
                amountFormat(totalAmount),
                amountFormat(subsidy),
                amountFormat(copay),
                status
        );
        return CustomApiResponse.createSuccess(HttpStatus.OK.value(),"신청 내역 세부내용 조회에 성공하였습니다.", applyDetailDto);
    }

    //서비스 시작 시간과 종료 시갼을 매개변수로 받아 총 금액을 계산함
    public Long calculateTotalAmount(LocalDateTime startDate, LocalDateTime endDate){
        final long RATE_PER_HOUR = 13900;
        long hours = Duration.between(startDate, endDate).toHours(); // 이용 시간 계산
        // 총 이용 금액
        return hours * RATE_PER_HOUR;
    }

    //지원금 계산 메소드
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

    //현재 서비스 상태 계산 메소드
    public Status getNowStatus(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate)) {
            return Status.YET;
        } else if (!now.isAfter(endDate)) {
            return Status.IN_PROGRESS;
        } else {
            return Status.COMPLETE;
        }
    }

    //신청날짜 데이터 포맷 메소드
    public String getApplyDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

    //서비스 이용 시간 데이터 포맷 메소드
    public String getCareTime(LocalDateTime startDate, LocalDateTime endDate) {
        String startTime = startDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        String endTime = endDate.format(DateTimeFormatter.ofPattern("HH:mm"));
        return startTime + " ~ " + endTime;
    }

    //서비스 상태
    public String getStatus(Status Status) {
        switch (Status) {
            case YET: return "서비스 신청 완료";
            case IN_PROGRESS: return "돌봄 서비스 이용 중";
            case COMPLETE: return "이용 완료";
            default: return "";
        }
    }

    //금액 포맷 메소드
    public String amountFormat(Long amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US); // US 스타일
        return numberFormat.format(amount);
    }
}
