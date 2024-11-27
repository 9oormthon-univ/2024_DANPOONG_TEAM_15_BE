package com.ivory.ivory.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="caregiver")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Caregiver {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caregiver_seq")
    @SequenceGenerator(
            name = "caregiver_seq",                // 시퀀스 제너레이터 이름
            sequenceName = "caregiver_sequence",  // 실제 데이터베이스 시퀀스 이름
            initialValue = 100,                   // 시작 값
            allocationSize = 1                    // 증가 값
    )
    @Column(name="id")
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public static Caregiver toEntity(String email, String password) {
        return Caregiver.builder()
                .email(email)
                .password(password)
                .authority(Authority.ROLE_CAREGIVER)
                .build();
    }
}
