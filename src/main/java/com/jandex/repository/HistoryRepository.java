package com.jandex.repository;

import com.jandex.entity.Category;
import com.jandex.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
 List<History> findAllByCategory(Category category);
}
