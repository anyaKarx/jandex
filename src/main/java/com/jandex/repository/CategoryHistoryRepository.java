package com.jandex.repository;

import com.jandex.entity.CategoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryHistoryRepository extends JpaRepository<CategoryHistory, UUID> {
    Optional<List<CategoryHistory>> findAllByDateBetween(LocalDateTime dateTime, LocalDateTime dateT);

    Optional<List<CategoryHistory>> findAllByDateBetweenAndParent_Id(LocalDateTime dateTime, LocalDateTime dateT, UUID id);
}