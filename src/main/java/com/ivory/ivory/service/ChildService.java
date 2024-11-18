package com.ivory.ivory.service;

import com.ivory.ivory.domain.Child;
import com.ivory.ivory.domain.Member;
import com.ivory.ivory.dto.ChildRequestDto;
import com.ivory.ivory.repository.ChildRepository;
import com.ivory.ivory.repository.MemberRepository;
import com.ivory.ivory.util.response.CustomApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildService {


    private final MemberRepository memberRepository;
    private final ChildRepository childRepository;

    public CustomApiResponse<?> addChild(ChildRequestDto dto, Long memberId) {

        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            CustomApiResponse response = CustomApiResponse.createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다.");
            return response;
        }

        //엔티티 생성
        Child child = Child.toEntity(dto,member.get());
        //DB에 저장
        childRepository.save(child);

        //응답 생성
        CustomApiResponse response = CustomApiResponse.createSuccess(HttpStatus.OK.value(),"자녀 정보가 성공적으로 등록되었습니다.",null);

        return response;
    }
}

