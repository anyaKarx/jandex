package com.jandex.repository;

import com.jandex.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {

 @Query(value="select * from history order by date desc limit 1", nativeQuery = true)
 History findHistoryByDate_MaxAndIdParent(UUID id);
}
