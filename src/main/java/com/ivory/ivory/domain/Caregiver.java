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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
