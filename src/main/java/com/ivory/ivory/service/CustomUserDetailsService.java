package com.ivory.ivory.service;

import com.ivory.ivory.domain.Caregiver;
import com.ivory.ivory.domain.Member;
import com.ivory.ivory.repository.CaregiverRepository;
import com.ivory.ivory.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CaregiverRepository caregiverRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Member 테이블에서 이메일을 검색
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                // Member 테이블에 없으면 Caregiver 테이블에서 이메일 검색
                .or(() -> caregiverRepository.findByEmail(username).map(this::createUserDetails))
                // 둘 다 없을 경우 예외 발생
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }


    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    private UserDetails createUserDetails(Caregiver caregiver) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(caregiver.getAuthority().toString());

        return new User(
                String.valueOf(caregiver.getId()),
                caregiver.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

}