package com.jandex.repository;

import com.jandex.entity.History;
import com.jandex.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {

    void deleteAllByParent(Offer parent);
}
