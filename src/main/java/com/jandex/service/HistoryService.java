package com.jandex.service;


import com.jandex.entity.History;
import com.jandex.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public History save(History history) {
        return historyRepository.save(history);
    }

    public Optional<List<History>> getHistoriesByData(LocalDateTime date, LocalDateTime date2)
    {
        return historyRepository.findAllByDateBetween(date, date2);
    }
}
