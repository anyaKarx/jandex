package com.jandex.repository;

import com.jandex.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findAllByDateBetween(LocalDateTime dateTime, LocalDateTime dateT);

    List<History> findAllByDateBetweenAndIdParent(LocalDateTime dateTime, LocalDateTime dateT, UUID id);
}
