package com.ivory.ivory.repository;

import com.ivory.ivory.domain.Authority;
import com.ivory.ivory.domain.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    Optional<Caregiver> findByEmail(String username);
    Authority findAuthorityByEmail(String email);
}

