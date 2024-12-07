package com.ivory.ivory.repository;

import com.ivory.ivory.domain.Apply;
import com.ivory.ivory.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByChild_IdOrderByCreateAtDesc(Long childId);
    Optional<Apply> findById (Long applyId);
    Apply findFirstByChild_IdOrderByCreateAtDesc(Long childID);
    Optional<Apply> findFirstByStatusOrderByCreateAtDesc(Status status);
    List<Apply> findAllByStatus(Status status);
    List<Apply> findAllByOrderByCreateAtDesc();
}
