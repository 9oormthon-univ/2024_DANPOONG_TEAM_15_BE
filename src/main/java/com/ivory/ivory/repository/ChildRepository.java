package com.ivory.ivory.repository;

import com.ivory.ivory.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findAllByMember_Id(Long memberId);
    Optional<Child> findById(Long childId);
}
