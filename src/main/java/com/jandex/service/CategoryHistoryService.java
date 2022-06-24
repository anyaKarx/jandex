package com.jandex.service;

import com.jandex.entity.CategoryHistory;
import com.jandex.repository.CategoryHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryHistoryService {
    private final CategoryHistoryRepository historyRepository;

    @Transactional
    public CategoryHistory save(CategoryHistory history){

        return historyRepository.save(history);
    }

    public Optional<List<CategoryHistory>> getHistoriesByData(LocalDateTime date, LocalDateTime date2) {
        return historyRepository.findAllByDateBetween(date, date2);
    }

    public Optional<List<CategoryHistory>> getHistoriesByDataAndOffer(LocalDateTime date, LocalDateTime date2, UUID id) {
        return historyRepository.findAllByDateBetweenAndParent_Id(date, date2, id);
    }
}
