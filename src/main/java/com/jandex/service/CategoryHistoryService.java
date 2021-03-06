package com.jandex.service;

import com.jandex.entity.CategoryHistory;
import com.jandex.repository.CategoryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryHistoryService {
    private final CategoryHistoryRepository historyRepository;

    public List<CategoryHistory> getHistoriesByDataAndCategory(LocalDateTime date, LocalDateTime date2, UUID id) {
        return historyRepository.findAllByDateBetweenAndIdParent(date, date2, id);
    }
}
