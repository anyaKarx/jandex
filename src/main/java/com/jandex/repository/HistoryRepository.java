package com.jandex.repository;

import com.jandex.entity.History;
import com.jandex.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {

    Optional<List<History>> findAllByDateBetween(LocalDateTime dateTime, LocalDateTime dateT);

    Optional<History> findAllByDateContains(LocalDateTime date);
    void deleteAllByParent(Offer parent);
}
