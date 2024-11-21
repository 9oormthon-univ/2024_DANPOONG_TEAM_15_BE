package com.ivory.ivory.repository;

import com.ivory.ivory.domain.AbsenceCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceCertificateRepository extends JpaRepository<AbsenceCertificate, Long> {
    Page<AbsenceCertificate> findAllByChildId(Long childId, Pageable pageable);
}
