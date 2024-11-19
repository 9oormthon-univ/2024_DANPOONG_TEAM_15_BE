package com.ivory.ivory.repository;

import com.ivory.ivory.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    List<Apply> findAllByChild_Id(Long childId);
}
