package com.ivory.ivory.util.init;

import com.ivory.ivory.domain.Caregiver;
import com.ivory.ivory.repository.CaregiverRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CaregiverRepository caregiverRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        createCaregiver();
    }

    public void createCaregiver() {
        if (caregiverRepository.count() == 0) {

            //비밀번호 암호화
            String encodedPassword = passwordEncoder.encode("1234");

            Caregiver caregiver = Caregiver.toEntity(
                    "admin@gmail.com",
                    encodedPassword
            );

            caregiverRepository.save(caregiver);
        }
    }
}
