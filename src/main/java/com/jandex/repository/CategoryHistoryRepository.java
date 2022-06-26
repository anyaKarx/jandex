package com.jandex.repository;

import com.jandex.entity.CategoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryHistoryRepository extends JpaRepository<CategoryHistory, UUID> {
   List<CategoryHistory> findAllByDateBetweenAndIdParent(LocalDateTime dateTime, LocalDateTime dateT, UUID id);
}