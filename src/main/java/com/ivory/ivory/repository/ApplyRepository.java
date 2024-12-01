package com.ivory.ivory.repository;

import com.ivory.ivory.domain.Apply;
import com.ivory.ivory.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByChild_Id(Long childId);
    Optional<Apply> findById (Long applyId);
    Apply findFirstByChild_IdOrderByCreateAt(Long childID);
    Optional<Apply> findFirstByStatusOrderByCreateAtDesc(Status status);

}
