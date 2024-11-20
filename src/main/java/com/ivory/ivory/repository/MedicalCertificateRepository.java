package com.ivory.ivory.repository;

import com.ivory.ivory.domain.MedicalCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalCertificateRepository extends JpaRepository<MedicalCertificate, Long> {
    Page<MedicalCertificate> findAllByChildId(Long childId, Pageable pageable);
}
