package com.jandex.service;


import com.jandex.entity.Category;
import com.jandex.entity.History;
import com.jandex.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.summarizingLong;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public History save(History history) {
        return historyRepository.save(history);
    }

    public Long getPrice(Category category) {
        List<History> histories = historyRepository.findAllByCategory(category);
        if (histories != null) {
            var price = histories
                    .stream()
                    .collect(summarizingLong(History::getPrice)).getAverage();

            return Double.doubleToLongBits(price);
        }else return Long.valueOf(0);
    }
}
